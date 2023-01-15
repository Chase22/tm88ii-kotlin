  int ERROR = 2;
  int STROBE = 3;
  int DATA_0 = 4;
  int DATA_1 = 5;
  int DATA_2 = 6;
  int DATA_3 = 7;
  int DATA_4 = 8;
  int DATA_5 = 9;
  int DATA_6 = 10;
  int DATA_7 = 11;

  int BUSY = 12;
  int PE = 13;

void setup() {
  pinMode(DATA_0, OUTPUT);
  pinMode(DATA_1, OUTPUT);
  pinMode(DATA_2, OUTPUT);
  pinMode(DATA_3, OUTPUT);
  pinMode(DATA_4, OUTPUT);
  pinMode(DATA_5, OUTPUT);
  pinMode(DATA_6, OUTPUT);
  pinMode(DATA_7, OUTPUT);

  pinMode(STROBE, OUTPUT);

  pinMode(ERROR, INPUT_PULLUP);
  pinMode(BUSY, INPUT);
  pinMode(PE, INPUT);

  digitalWrite(STROBE, HIGH);

  // for(byte value = 0; value < 255; value++) {
  //   writeByte(value);
  // }

  Serial.begin(9600, SERIAL_8O2);
}

void writeByte(byte data) {
  digitalWrite(DATA_0, bitRead(data, 0));
  digitalWrite(DATA_1, bitRead(data, 1));
  digitalWrite(DATA_2, bitRead(data, 2));
  digitalWrite(DATA_3, bitRead(data, 3));
  digitalWrite(DATA_4, bitRead(data, 4));
  digitalWrite(DATA_5, bitRead(data, 5));
  digitalWrite(DATA_6, bitRead(data, 6));
  digitalWrite(DATA_7, bitRead(data, 7));

  digitalWrite(STROBE, LOW);
  digitalWrite(STROBE, HIGH);

  int busy = 1;

  while (busy == 1) {
    busy = digitalRead(BUSY);
  }

}

void loop() {
  if (Serial.available() > 0) {
    byte received = Serial.read();

    writeByte(received);

    byte status = 0;
    bitWrite(status, 0, digitalRead(BUSY));
    bitWrite(status, 1, digitalRead(PE));
    bitWrite(status, 2, digitalRead(ERROR));

    Serial.write(status);
    Serial.flush();
  }
}
