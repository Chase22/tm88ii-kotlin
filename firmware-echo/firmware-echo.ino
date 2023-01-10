void setup() {
  Serial.begin(9600);
}

void loop() {
  if (Serial.available() > 0) {
    byte received = Serial.read();

    Serial.write(received);
    Serial.flush();
  }
}
