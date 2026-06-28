# QDCA10Pro マイクロサービス

## 概要

QDCA10Pro はドローンショップの **ドローンB 製造マイクロサービス** です。

- Counter から Kafka 経由で製造指示を受信
- ドローンBの製造ビジネスロジックを実行（QDCA10 より高性能モデル）
- 製造完了後、Counter へ完了通知を送信

**フレームワーク**: Quarkus  
**デプロイ先クラスター**: b-cluster

---

## アーキテクチャ

```
Counter（a-cluster）
        │
        ▼ Kafka: qdca10pro-in（MirrorMaker2 経由）
┌─────────────────┐
│   QDCA10Pro     │ ── 製造ビジネスロジック実行
│                 │
│                 │──► Kafka: orders-up（製造完了通知）
└─────────────────┘
                        │
                        ▼ MirrorMaker2
                Counter（a-cluster）
```

### Kafka トピック一覧

| トピック | 方向 | 説明 |
|---------|------|------|
| `qdca10pro-in` | 受信 | Counter からの製造指示 |
| `orders-up` | 送信 | 製造完了通知 |

### メッセージ形式（OrderTicket）

```json
{
  "orderId": "uuid",
  "item": "DRONE_B",
  "preparedBy": "QDCA10Pro"
}
```

---

## ローカル開発

### 前提条件

- Java 17+
- Docker / Docker Compose

### 1. インフラ起動

```shell
git clone https://github.com/quarkusdroneshop/quarkusdroneshop-support.git
cd quarkusdroneshop-support
docker compose up -d
```

### 2. アプリケーション起動

```shell
git clone https://github.com/quarkusdroneshop/quarkusdroneshop-qdca10pro.git
cd quarkusdroneshop-qdca10pro
./mvnw clean compile quarkus:dev
```

### 3. テストメッセージ送信

```shell
kafka-console-producer --broker-list localhost:9092 --topic qdca10pro-in
> {"orderId":"test-001","item":"DRONE_B","quantity":1}
```

### 環境変数

| 変数名 | デフォルト | 説明 |
|--------|-----------|------|
| `KAFKA_BOOTSTRAP_URLS` | `localhost:9092` | Kafka ブートストラップアドレス |

---

## 本番デプロイ（Tekton Pipeline）

### パイプライン概要

```
fetch-repository → semgrep-scan → maven-run → push-oc-apps
```

### 手動実行

```shell
tkn pipeline start build-and-push-quarkusdroneshop-qdca10pro \
  -n quarkusdroneshop-cicd \
  --use-param-defaults
```

RHDH の **CI タブ** からパイプライン実行状況を確認できます。

---

## テスト

```shell
# ユニットテスト(ArchUnit含む)
./mvnw test

# 統合テスト（Jacoco含む）
./mvnw verify

# チェックスタイル
./mvnw checkstyle:check

# PMD
./mvnw pmd:pmd

# SpotBugs
./mvnw spotbugs:spotbugs

# semgrep
semgrep scan --config p/default --json > target/semgrep-results.json

# secret scan
gitleaks detect --source . --report-format json --report-path target/gitleaks-report.json --exit-code 1

# 脆弱性テスト
trivy fs --scanners vuln,secret,misconfig,license --exit-code=1 --ignorefile ./.trivyignore.yaml ./ > target/trivy.txt

# セキュリティテスト
mvn quarkus:dev > quarkus.log 2>&1 & QUARKUS_PID=$!; sleep 10; wapiti -u http://localhost:8080 -f json -o ./target/wapiti.json; kill $QUARKUS_PID

# テストレポートの作成
./mvnw exec:exec@generate-report
```

---

## 注意事項

- **QDCA10 との違い**: QDCA10 はドローンA、QDCA10Pro はドローンBを製造。Pro モデルは追加の品質チェックステップがあります。
- **クラスター間 Kafka**: `qdca10pro-in` は a-cluster の Kafka から MirrorMaker2 でミラーリング。
- **スケーリング**: 注文量に応じて b-cluster でのレプリカ数を調整してください。
