import json
from time import sleep

from kafka import KafkaProducer
from kafka.admin import KafkaAdminClient, NewTopic
from faker import Faker

def main():
    admin = KafkaAdminClient(bootstrap_servers="localhost:9092")
    topics = admin.list_topics()

    if "vehicle_order" not in topics:
        admin.create_topics([
            NewTopic("vehicle_order", 1, 1)
        ])
        sleep(3)


    generator = Faker()

    producer = KafkaProducer(
        bootstrap_servers="localhost:9092",
        key_serializer=str.encode,
        value_serializer=lambda v: json.dumps(v).encode("utf-8"),
    )

    for index in range(1000):
        vechile_order_id = generator.uuid4()
        producer.send(
            "vehicle_order",
            key=generator.uuid4(),
            value={
                "id": vechile_order_id,
                "vehicleNumber": index,
                "statue": "created"
            }
        )
    producer.flush()


if __name__ == "__main__":
    main()