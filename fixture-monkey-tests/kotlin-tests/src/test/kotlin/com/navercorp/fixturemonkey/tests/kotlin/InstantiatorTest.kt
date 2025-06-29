package com.navercorp.fixturemonkey.tests.kotlin

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.api.instantiator.Instantiator
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeKotlinBuilder
import com.navercorp.fixturemonkey.kotlin.giveMeOne
import com.navercorp.fixturemonkey.kotlin.instantiator.instantiateBy
import com.navercorp.fixturemonkey.kotlin.setExp
import com.navercorp.fixturemonkey.tests.TestEnvironment.TEST_COUNT
import org.assertj.core.api.BDDAssertions.then
import org.junit.jupiter.api.RepeatedTest
import java.lang.reflect.Modifier

class InstantiatorTest {
    @RepeatedTest(TEST_COUNT)
    fun instantiateJavaPrimaryConstructor() {
        val actual = SUT.giveMeKotlinBuilder<Foo>()
            .instantiateBy {
                Instantiator.constructor<Foo>()
                    .parameter(String::class.java, "foo")
                    .parameter(Int::class.java, "bar")
            }
            .sample()
            .bar

        then(actual).isNotNull
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiatePrimaryConstructor() {
        val actual = SUT.giveMeKotlinBuilder<Foo>()
            .instantiateBy {
                constructor<Foo> {
                    parameter<String>("foo")
                    parameter<Int>("bar")
                }
            }
            .sample()
            .bar

        then(actual).isNotNull
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateSecondaryConstructor() {
        val actual = SUT.giveMeKotlinBuilder<Foo>()
            .instantiateBy {
                constructor<Foo> {
                    parameter<String>("foo")
                }
            }
            .sample()
            .bar

        then(actual).isEqualTo(1)
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiatePrimaryConstructorWithoutParameterName() {
        val actual = SUT.giveMeKotlinBuilder<Foo>()
            .instantiateBy {
                constructor<Foo> {
                    parameter<String>()
                    parameter<Int>()
                }
            }
            .sample()
            .bar

        then(actual).isNotNull
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateSecondaryConstructorWithoutParameterName() {
        val actual = SUT.giveMeKotlinBuilder<Foo>()
            .instantiateBy {
                constructor<Foo> {
                    parameter<String>()
                }
            }
            .sample()
            .bar

        then(actual).isEqualTo(1)
    }

    @RepeatedTest(TEST_COUNT)
    fun setWithInstantiate() {
        val actual = SUT.giveMeKotlinBuilder<Foo>()
            .instantiateBy {
                constructor<Foo> {
                    parameter<String>()
                }
            }
            .set("foo", "bar")
            .sample()
            .foo

        then(actual).isEqualTo("bar")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateRootType() {
        val actual = SUT.giveMeKotlinBuilder<Foo>()
            .instantiateBy {
                constructor {
                    parameter<String>()
                }
            }
            .sample()
            .bar

        then(actual).isEqualTo(1)
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateGenericType() {
        val actual = SUT.giveMeKotlinBuilder<Bar<String>>()
            .instantiateBy {
                constructor<Bar<String>> {
                    parameter<String>()
                }
            }
            .sample()
            .bar

        then(actual).isNotNull
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateByConstructorTwoTypes() {
        val actual = SUT.giveMeKotlinBuilder<Baz>()
            .instantiateBy {
                constructor<Foo> {
                    parameter<String>()
                }
                constructor<Bar<String>> {
                    parameter<String>()
                }
            }
            .sample()

        then(actual).isNotNull
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateByMixedTwoTypes() {
        val actual = SUT.giveMeKotlinBuilder<Baz>()
            .instantiateBy {
                constructor<Bar<String>> {
                    parameter<String>()
                }
                factory<Foo>("build") {
                    parameter<Int>()
                }
            }
            .sample()

        then(actual).isNotNull
        then(actual.foo.foo).isEqualTo("factory")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateByCompanionObjectFactoryMethodWithoutType() {
        val actual = SUT.giveMeKotlinBuilder<Foo>()
            .instantiateBy {
                factory("build")
            }
            .sample()
            .foo

        then(actual).isEqualTo("factory")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateByCompanionObjectFactoryMethod() {
        val actual = SUT.giveMeKotlinBuilder<Foo>()
            .instantiateBy {
                factory<Foo>("build")
            }
            .sample()
            .foo

        then(actual).isEqualTo("factory")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateByCompanionObjectFactoryMethodWithoutTypeWithParameter() {
        val actual = SUT.giveMeKotlinBuilder<Foo>()
            .instantiateBy {
                factory("build") {
                    parameter<Int>()
                }
            }
            .sample()
            .foo

        then(actual).isEqualTo("factory")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateByCompanionObjectFactoryMethodWithParameter() {
        val actual = SUT.giveMeKotlinBuilder<Foo>()
            .instantiateBy {
                factory<Foo>("build") {
                    parameter<Int>()
                }
            }
            .sample()
            .foo

        then(actual).isEqualTo("factory")
    }

    @RepeatedTest(TEST_COUNT)
    fun setInstantiateByCompanionObjectFactoryMethodWithParameter() {
        val actual = SUT.giveMeKotlinBuilder<Foo>()
            .instantiateBy {
                factory<Foo>("build") {
                    parameter<Int>()
                }
            }
            .set("factoryBar", 1)
            .sample()
            .bar

        then(actual).isEqualTo(1)
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateJavaObjectByRootProperty() {
        val actual = SUT.giveMeKotlinBuilder<JavaConstructorTestSpecs.JavaTypeObject>()
            .instantiateBy {
                constructor {
                    property()
                }
            }
            .sample()
            .string

        then(actual).isNotEqualTo("string")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateJavaObjectByRootPropertyFilter() {
        val actual = SUT.giveMeKotlinBuilder<JavaConstructorTestSpecs.JavaTypeObject>()
            .instantiateBy {
                constructor {
                    property {
                        filter { !it.isFinal }
                    }
                }
            }
            .sample()
            .string

        then(actual).isEqualTo("third")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateJavaObjectByRootField() {
        val actual = SUT.giveMeKotlinBuilder<JavaConstructorTestSpecs.JavaTypeObject>()
            .instantiateBy {
                constructor {
                    javaField()
                }
            }
            .sample()
            .string

        then(actual).isNotEqualTo("string")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateJavaObjectByRootFieldFilter() {
        val actual = SUT.giveMeKotlinBuilder<JavaConstructorTestSpecs.JavaTypeObject>()
            .instantiateBy {
                constructor {
                    javaField {
                        filter { !Modifier.isFinal(it.modifiers) }
                    }
                }
            }
            .sample()
            .string

        then(actual).isEqualTo("third")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateJavaObjectByRootJavaBeans() {
        val actual = SUT.giveMeKotlinBuilder<JavaConstructorTestSpecs.JavaTypeObject>()
            .instantiateBy {
                constructor {
                    javaBeansProperty()
                }
            }
            .sample()
            .string

        then(actual).isNotEqualTo("string")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateJavaObjectByRootFieldJavaBeans() {
        val actual = SUT.giveMeKotlinBuilder<JavaConstructorTestSpecs.JavaTypeObject>()
            .instantiateBy {
                constructor {
                    javaBeansProperty {
                        filter { "string" != it.name }
                    }
                }
            }
            .sample()
            .string

        then(actual).isEqualTo("third")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiatePropertyByKotlinProperty() {
        class PropertyObject {
            var string: String = "test"
        }

        class ConstructorObject(val propertyObject: PropertyObject)

        val actual = SUT.giveMeKotlinBuilder<ConstructorObject>()
            .instantiateBy { constructor<PropertyObject> { property() } }
            .sample()
            .propertyObject
            .string

        then(actual).isNotEqualTo("test")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiatePropertyByKotlinPropertyFilter() {
        class PropertyObject {
            var string: String = "test"
        }

        class ConstructorObject(val propertyObject: PropertyObject)

        val actual = SUT.giveMeKotlinBuilder<ConstructorObject>()
            .instantiateBy {
                constructor<PropertyObject> {
                    property {
                        filter {
                            it.name != "string"
                        }
                    }
                }
            }
            .sample()
            .propertyObject
            .string

        then(actual).isEqualTo("test")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiatePropertyByKotlinPropertyPrivateSetter() {
        class PropertyObject {
            var string: String = "test"
                private set
        }

        class ConstructorObject(val propertyObject: PropertyObject)

        val actual = SUT.giveMeKotlinBuilder<ConstructorObject>()
            .instantiateBy { constructor<PropertyObject> { property() } }
            .sample()
            .propertyObject
            .string

        then(actual).isEqualTo("test")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateNoArgsConstructor() {
        class NoArgsConstructorObject() {
            var string: String = "noArgs"

            constructor(string: String) : this() {
                this.string = string
            }
        }

        val actual = SUT.giveMeKotlinBuilder<NoArgsConstructorObject>()
            .instantiateBy { constructor() }
            .sample()
            .string

        then(actual).isEqualTo("noArgs")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiatePropertyNoArgsConstructor() {
        class NoArgsConstructorObject {
            var string: String = "noArgs"
        }

        class AllArgsConstructorObject(val value: NoArgsConstructorObject)

        val actual = SUT.giveMeKotlinBuilder<AllArgsConstructorObject>()
            .instantiateBy { constructor<NoArgsConstructorObject>() }
            .sample()
            .value
            .string

        then(actual).isEqualTo("noArgs")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateCompositeJavaObjectByProperty() {
        class ConstructorAndProperty(val value: String) {
            var propertyNotInConstructor: String? = null
        }

        val actual = SUT.giveMeKotlinBuilder<ConstructorAndProperty>()
            .instantiateBy {
                constructor {
                    parameter<String>()
                    property()
                }
            }
            .setNotNull("propertyNotInConstructor")
            .sample()

        then(actual.value).isNotNull()
        then(actual.propertyNotInConstructor).isNotNull
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiatePrivateConstructor() {
        class PrivateConstructorObject private constructor(val value: String)

        val actual = SUT.giveMeKotlinBuilder<PrivateConstructorObject>()
            .instantiateBy {
                constructor()
            }
            .sample()
            .value

        then(actual).isNotNull()
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiatePrivateConstructorWithoutInstantiate() {
        class PrivateConstructorObject private constructor(val value: String)

        val actual = SUT.giveMeOne<PrivateConstructorObject>()
            .value

        then(actual).isNotNull()
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateDefaultArgumentConstructor() {
        class ConstructorObject(val value: String = "default")

        val actual = SUT.giveMeKotlinBuilder<ConstructorObject>()
            .instantiateBy { constructor() }
            .sample()
            .value

        then(actual).isNotEqualTo("default")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateJavaConstructorWithDefaultArgument() {
        class ConstructorObject(val value: String = "default")

        val actual = SUT.giveMeKotlinBuilder<ConstructorObject>()
            .instantiateBy {
                Instantiator.constructor<ConstructorObject>()
            }
            .sample()
            .value

        then(actual).isNotEqualTo("default")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateConstructorWithDefaultArgument() {
        class ConstructorObject(val value: String = "default")

        val actual = SUT.giveMeKotlinBuilder<ConstructorObject>()
            .instantiateBy {
                constructor {
                    parameter<String>(useDefaultArgument = true)
                }
            }
            .sample()
            .value

        then(actual).isEqualTo("default")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateByCompanionObjectFactoryMethodInRegister() {
        val sut = FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .register(Foo::class.java) {
                it.giveMeKotlinBuilder<Foo>()
                    .instantiateBy {
                        factory("build") {
                            parameter<Int>()
                        }
                    }
            }
            .build()

        val actual = sut.giveMeOne<Foo>().foo

        then(actual).isEqualTo("factory")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateByConstructorMethodInRegister() {
        val sut = FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .register(Foo::class.java) {
                it.giveMeKotlinBuilder<Foo>()
                    .instantiateBy {
                        constructor {
                            parameter<String>()
                        }
                    }
            }
            .build()

        val actual = sut.giveMeOne<Foo>().bar

        then(actual).isEqualTo(1)
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateByInRegisterLatterWins() {
        val sut = FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .register(Foo::class.java) {
                it.giveMeKotlinBuilder<Foo>()
                    .instantiateBy {
                        factory("build") {
                            parameter<Int>()
                        }
                    }
                    .instantiateBy {
                        constructor {
                            parameter<String>()
                        }
                    }
            }
            .build()

        val actual = sut.giveMeOne<Foo>().bar

        then(actual).isEqualTo(1)
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateByPropertyInRegister() {
        val sut = FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .register(Foo::class.java) {
                it.giveMeKotlinBuilder<Foo>()
                    .instantiateBy {
                        factory("build") {
                            parameter<Int>()
                        }
                    }
            }
            .build()

        val actual = sut.giveMeOne<FooWrapper>().foo.foo

        then(actual).isEqualTo("factory")
    }

    @RepeatedTest(TEST_COUNT)
    fun instantiateByPropertySetFactoryPropertyInRegister() {
        val sut = FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .register(Foo::class.java) {
                it.giveMeKotlinBuilder<Foo>()
                    .instantiateBy {
                        factory("build") {
                            parameter<Int>()
                        }
                    }
            }
            .build()
        val expected = 2

        val actual = sut.giveMeBuilder<FooWrapper>()
            .set("foo.factoryBar", expected)
            .sample()
            .foo
            .bar

        then(actual).isEqualTo(expected)
    }

    class FooWrapper(val foo: Foo)

    class Foo(val foo: String, val bar: Int) {
        constructor(foo: String) : this(foo, 1)

        companion object {
            fun build(factoryBar: Int): Foo = Foo("factory", factoryBar)
        }
    }

    class Bar<T>(val bar: T)

    class Baz(val foo: Foo, val bar: Bar<String>)

    companion object {
        private val SUT = FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .build()
    }
}
