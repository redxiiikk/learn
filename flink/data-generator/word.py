from faker import Faker
from kafka import KafkaProducer


def main():
    generator = Faker()

    producer = KafkaProducer(
        bootstrap_servers="localhost:9092",
        key_serializer=str.encode,
        value_serializer=str.encode,
    )

    for _ in range(500000):
        producer.send(
            "word", key=generator.uuid4(), value=generator.paragraph(nb_sentences=5)
        )
    producer.flush()


if __name__ == "__main__":
    main()
