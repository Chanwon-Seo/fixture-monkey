/*
 * Fixture Monkey
 *
 * Copyright (c) 2021-present NAVER Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.navercorp.fixturemonkey.tests.java.specs;

import lombok.Builder;
import lombok.Value;

@SuppressWarnings("unused")
public class GenericTypeSpecs {

	public interface GenericInterface<T> {
	}

	@Value
	@Builder
	public static class GenericImplementationObject<T> implements GenericInterface<T> {
		T value;
	}

	public interface TwoGenericInterface<T, U> {
	}

	@Value
	@Builder
	public static class TwoGenericImplementationObject<T, U> implements TwoGenericInterface<T, U> {
		T tValue;

		U uValue;
	}

	@Value
	@Builder
	public static class GenericObject<T> {
		T value;
	}

	@Value
	@Builder
	public static class GenericArrayObject<T> {
		GenericImplementationObject<T>[] values;
	}

	@Value
	@Builder
	public static class TwoGenericObject<T, U> {
		T value1;
		U value2;
	}

	@Value
	public static class TwoGenericObjectConstructorParameterOrderDiff<T, U> {
		T value1;
		U value2;

		public TwoGenericObjectConstructorParameterOrderDiff(U value2, T value1) {
			this.value1 = value1;
			this.value2 = value2;
		}
	}

	@Value
	public static class TwoGenericObjectConstructorParameterOrderDiffNameDiff<T, U> {
		T value1;
		U value2;

		public TwoGenericObjectConstructorParameterOrderDiffNameDiff(U value1, T value2) {
			this.value1 = value2;
			this.value2 = value1;
		}
	}

	@Value
	@Builder
	public static class ThreeGenericObject<T, U, V> {
		T value1;
		U value2;
		V value3;
	}

	public interface Interface {
	}
}
