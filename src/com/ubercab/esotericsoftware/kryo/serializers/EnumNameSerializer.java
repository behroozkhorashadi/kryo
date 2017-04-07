
package com.ubercab.esotericsoftware.kryo.serializers;

import com.ubercab.esotericsoftware.kryo.Kryo;
import com.ubercab.esotericsoftware.kryo.KryoException;
import com.ubercab.esotericsoftware.kryo.Serializer;
import com.ubercab.esotericsoftware.kryo.io.Input;
import com.ubercab.esotericsoftware.kryo.io.Output;

/** Serializes enums using the enum's name. This prevents invalidating previously serialized byts when the enum order changes.
 * @author KwonNam Son <kwon37xi@gmail.com> */
public class EnumNameSerializer extends Serializer<Enum> {
	private final Class<? extends Enum> enumType;
	private final Serializer stringSerializer;

	public EnumNameSerializer (Kryo kryo, Class<? extends Enum> type) {
		this.enumType = type;
		stringSerializer = kryo.getSerializer(String.class);
		setImmutable(true);
	}

	public void write (Kryo kryo, Output output, Enum object) {
		kryo.writeObject(output, object.name(), stringSerializer);
	}

	public Enum read (Kryo kryo, Input input, Class<Enum> type) {
		String name = kryo.readObject(input, String.class, stringSerializer);
		try {
			return Enum.valueOf(enumType, name);
		} catch (IllegalArgumentException e) {
			throw new KryoException("Invalid name for enum \"" + enumType.getName() + "\": " + name, e);
		}
	}
}
