import json
from time import sleep

from faker import Faker
from kafka import KafkaProducer


def main():
    generator = Faker()

    producer = KafkaProducer(
        bootstrap_servers="localhost:9092",
        key_serializer=str.encode,
        value_serializer=lambda v: json.dumps(v).encode("utf-8"),
    )

    messages = {}
    for _ in range(30000):
        event_time = generator.date_time_this_century()
        messages[int(event_time.timestamp() * 1000)] = {
            "id": generator.uuid4(),
            "time": str(event_time),
            "amount": abs(generator.pyfloat(left_digits=1, right_digits=6)),
            "currency": generator.currency_code(),
            "creditCardId": generator.bban(),
            "merchantId": generator.pyint(max_value=100),
        }

    for timestamp in sorted(messages):
        # print(messages[timestamp])
        producer.send("cc_payments", key=generator.uuid4(), value=messages[timestamp])
    producer.flush()


if __name__ == "__main__":

    main()
