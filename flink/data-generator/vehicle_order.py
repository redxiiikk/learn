import json

from kafka import KafkaProducer
from faker import Faker


def main():
    generator = Faker()

    producer = KafkaProducer(
        bootstrap_servers="localhost:9092",
        key_serializer=str.encode,
        value_serializer=lambda v: json.dumps(v).encode("utf-8"),
    )

    for index in range(30):
        vechile_order_id = generator.uuid4()
        producer.send(
            "vehicle_order",
            key=vechile_order_id,
            value={
                "id": vechile_order_id,
                "vehicleNumber": index % 30,
                "statue": "created",
            },
        )
    producer.flush()


if __name__ == "__main__":
    main()
