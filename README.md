# quarkusdroneshop-qdca10pro

Quarkus ベースのドリンク製造マイクロサービス (QDCA10Pro モデル)。QDCA10 と同様のロールを持ちますが、Pro グレードのドリンク (エスプレッソ系) を担当します。

## アーキテクチャ

```
quarkusdroneshop-counter
    │  qdca10pro-in (dev) / shop-asite.qdca10pro-in (prod) ──▶
    ▼
quarkusdroneshop-qdca10pro
    │
    ├──▶ orders-up        (製造完了通知 → counter)
    └──▶ eighty-six       (在庫切れ通知)
```

## Kafka トピック

| チャネル | dev トピック | prod トピック | 方向 |
|---|---|---|---|
| orders-in | `qdca10pro-in` | `shop-asite.qdca10pro-in` | 受信 |
| orders-up | `orders-up` | `orders-up` | 送信 |
| eighty-six | `eighty-six` | `eighty-six` | 送信 |

## ローカル開発

```shell
git clone https://github.com/quarkusdroneshop/quarkusdroneshop-support.git
cd quarkusdroneshop-support
docker compose up

cd ../quarkusdroneshop-qdca10pro
./mvnw quarkus:dev
```

## 環境変数 (本番)

| 変数名 | 説明 |
|---|---|
| `KAFKA_BOOTSTRAP_URLS` | Kafka ブローカー URL |

## パッケージング

```shell
# JVM モード
./mvnw package
java -jar target/quarkusdroneshop-qdca10pro-1.0-SNAPSHOT-runner.jar

# ネイティブビルド
./mvnw package -Pnative -Dquarkus.native.container-build=true
./target/quarkusdroneshop-qdca10pro-1.0-SNAPSHOT-runner

# Docker 実行
docker run -i --network="host" \
  -e KAFKA_BOOTSTRAP_URLS=localhost:9092 \
  quarkusdroneshop-qdca10pro/quarkus-shop-QDCA10Pro:latest
```

## 参考

- [Quarkus](https://quarkus.io/)
- [quarkusdroneshop.github.io](https://quarkusdroneshop.github.io)
