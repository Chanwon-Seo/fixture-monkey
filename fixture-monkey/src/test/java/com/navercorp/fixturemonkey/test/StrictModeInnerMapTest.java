package com.navercorp.fixturemonkey.test;

import static org.assertj.core.api.BDDAssertions.thenNoException;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;

import java.util.Map;

import net.jqwik.api.Property;

import lombok.Data;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.customizer.InnerSpec;

public class StrictModeInnerMapTest {

	@Data
	public static class CustomObject {
		private Map<String, String> map;
		private Map<String, Map<String, String>> mapValueMap;
		private Map<String, CustomSimpleObject> mapValueObject;
	}

	@Data
	public static class CustomSimpleObject {
		private String str;
	}

	@Property
	void notStrictMode_nonExistentField() {
		FixtureMonkey sut = FixtureMonkey.builder().build();

		thenNoException()
			.isThrownBy(() -> sut.giveMeBuilder(CustomObject.class)
				.setInner(new InnerSpec()
					.property("nonExistentField", m -> m.minSize(1).key("key"))
				)
				.sample());
	}

	@Property
	void strictModeInnerSpecMapWrongExpressionThrows() {
		FixtureMonkey sut = FixtureMonkey.builder().useExpressionStrictMode().build();

		thenThrownBy(
			() -> sut.giveMeBuilder(CustomObject.class)
				.setInner(new InnerSpec()
					.property("nonExistentField", m -> m.size(1).entry("key", "value"))
				)
				.sample()
		).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("No matching results for given container expression.");
	}

	@Property
	void strictModeInnerSpecNestedMapWrongExpressionThrows() {
		FixtureMonkey sut = FixtureMonkey.builder().useExpressionStrictMode().build();

		thenThrownBy(
			() -> sut.giveMeBuilder(CustomObject.class)
				.setInner(new InnerSpec()
					.property("nonExistentField",
						m -> m.size(1).entry("key", v ->
							v.size(1).entry("nestedKey", "value")))
				)
				.sample()
		).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("No matching results for given container expression.");
	}

	@Property
	void strictModeInnerSpecCustomMapWrongExpressionThrows() {
		FixtureMonkey sut = FixtureMonkey.builder().useExpressionStrictMode().build();

		thenThrownBy(
			() -> sut.giveMeBuilder(CustomObject.class)
				.setInner(new InnerSpec()
					.property("nonExistentField", m -> m.size(1).value(v -> v.property("str", "value")))
				)
				.sample()
		).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("No matching results for given container expression.");
	}

	@Property
	void strictModeInnerSpecCustomMapNestedStringWrongExpressionThrows() {
		FixtureMonkey sut = FixtureMonkey.builder().useExpressionStrictMode().build();

		thenThrownBy(
			() -> sut.giveMeBuilder(CustomObject.class)
				.setInner(new InnerSpec()
					.property("mapValueObject", m -> m.size(1).value(v -> v.property("nonExistentField", "value")))
				)
				.sample()
		).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("No matching results for given container expression.");
	}

	@Property
	void strictModeMapValidExpressionDoesNotThrows() {
		FixtureMonkey sut = FixtureMonkey.builder().useExpressionStrictMode().build();

		thenNoException()
			.isThrownBy(() -> sut.giveMeBuilder(CustomObject.class)
				.setInner(new InnerSpec()
					.property("map", m -> m.minSize(1).key("key"))
				)
				.sample());
	}

}
