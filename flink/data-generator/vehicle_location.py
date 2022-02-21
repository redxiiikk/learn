import json
from kafka import KafkaProducer


def main():
    producer = KafkaProducer(
        bootstrap_servers="localhost:9092",
        key_serializer=str.encode,
        value_serializer=lambda v: json.dumps(v).encode("utf-8"),
    )

    for index in range(3000):
        vechile_order_id = index % 30
        producer.send(
            "vehicle_location",
            key=str(vechile_order_id),
            value={
                "id": vechile_order_id,
                "longitude": index * 0.001,
                "latitude": index * 0.001,
            },
        )
    producer.flush()


if __name__ == "__main__":
    main()
