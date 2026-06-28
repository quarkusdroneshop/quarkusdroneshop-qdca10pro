package io.quarkusdroneshop.qdca10pro;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * ArchUnit によるアーキテクチャ適合性テスト。
 * パッケージ構造:
 *   io.quarkusdroneshop.qdca10pro.domain.*            - ドメイン層 (エンティティ / イベント / 値オブジェクト)
 *   io.quarkusdroneshop.qdca10pro.domain.exceptions.* - ドメイン例外
 *   io.quarkusdroneshop.qdca10pro.domain.valueobjects.*- 値オブジェクト
 *   io.quarkusdroneshop.qdca10pro.infrastructure.*    - インフラ層 (Kafka / シリアライズ)
 */
@AnalyzeClasses(
        packages = "io.quarkusdroneshop.qdca10pro",
        importOptions = ImportOption.DoNotIncludeTests.class)
public class ArchitectureTest {

    // =========================================================================
    // 1. 命名規則
    // =========================================================================

    @ArchTest
    static final ArchRule Deserializer命名規則 =
        classes()
            .that().implement("org.apache.kafka.common.serialization.Deserializer")
            .or().areAssignableTo(
                io.quarkus.kafka.client.serialization.ObjectMapperDeserializer.class)
            .should().haveSimpleNameEndingWith("Deserializer");

    @ArchTest
    static final ArchRule Serializer命名規則 =
        classes()
            .that().implement("org.apache.kafka.common.serialization.Serializer")
            .and().resideInAPackage("io.quarkusdroneshop.qdca10pro..")
            .should().haveSimpleNameEndingWith("Serializer");

    @ArchTest
    static final ArchRule 例外クラスの命名規則 =
        classes()
            .that().areAssignableTo(Exception.class)
            .and().resideInAPackage("io.quarkusdroneshop.qdca10pro..")
            .should().haveSimpleNameEndingWith("Exception");

    // =========================================================================
    // 2. パッケージ配置ルール
    // =========================================================================

    @ArchTest
    static final ArchRule Deserializerはinfrastructureに配置 =
        classes()
            .that().haveSimpleNameEndingWith("Deserializer")
            .should().resideInAPackage("..infrastructure..");

    @ArchTest
    static final ArchRule Serializerはinfrastructureに配置 =
        classes()
            .that().haveSimpleNameEndingWith("Serializer")
            .and().resideInAPackage("io.quarkusdroneshop.qdca10pro..")
            .should().resideInAPackage("..infrastructure..");

    /**
     * ドメイン例外は domain.exceptions パッケージに配置されること。
     * (qdca10pro は domain.exceptions サブパッケージで例外を管理している)
     */
    @ArchTest
    static final ArchRule 例外はExceptionsパッケージに配置 =
        classes()
            .that().areAssignableTo(Exception.class)
            .and().resideInAPackage("io.quarkusdroneshop.qdca10pro..")
            .should().resideInAPackage("..exceptions..");

    // =========================================================================
    // 3. レイヤー間依存ルール
    // =========================================================================

    @ArchTest
    static final ArchRule ドメイン層はInfrastructureに依存しない =
        noClasses()
            .that().resideInAPackage("io.quarkusdroneshop.qdca10pro.domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("io.quarkusdroneshop.qdca10pro.infrastructure..");

    @ArchTest
    static final ArchRule 値オブジェクト層はInfrastructureに依存しない =
        noClasses()
            .that().resideInAPackage("io.quarkusdroneshop.qdca10pro.domain.valueobjects..")
            .should().dependOnClassesThat()
            .resideInAPackage("io.quarkusdroneshop.qdca10pro.infrastructure..");

    @ArchTest
    static final ArchRule ドメインクラスはPublic =
        classes()
            .that().resideInAPackage("io.quarkusdroneshop.qdca10pro.domain")
            .and().areNotInterfaces()
            .should().bePublic();

    @ArchTest
    static final ArchRule Infrastructureの依存範囲チェック =
        classes()
            .that().resideInAPackage("io.quarkusdroneshop.qdca10pro.infrastructure..")
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                "io.quarkusdroneshop.qdca10pro.infrastructure..",
                "io.quarkusdroneshop.qdca10pro.domain..",
                "java..",
                "javax..",
                "jakarta..",
                "io.quarkus..",
                "io.smallrye..",
                "org.eclipse.microprofile..",
                "org.apache.kafka..",
                "com.fasterxml..",
                "org.slf4j..",
                "org.jboss..");

    // =========================================================================
    // 4. 循環依存
    // =========================================================================

    @ArchTest
    static final ArchRule パッケージ間循環依存なし =
        slices()
            .matching("io.quarkusdroneshop.qdca10pro.(*)..")
            .should().beFreeOfCycles();
}
