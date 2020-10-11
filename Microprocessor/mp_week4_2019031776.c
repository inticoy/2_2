#include <avr/io.h>
#define FND_C0 0x01
#define FND_C1 0x02
#define FND_C2 0x04
#define FND_C3 0x08

const char Font[17] = {0x3F, 0x06, 0x5B, 0x4F, 0x66, 0x6D, 0x7D, 0x7F, 0x6F, 0x77, 0x7C,
	0x39, 0x5E, 0x79, 0x71, 0x00}; // 0 to F, Null

void delay(unsigned long x)
{
	while(x--);
}

int main(void)
{
	unsigned char digit0, digit1, digit2, digit3;
	unsigned long number;
	DDRB = 0xFF;
	DDRG = 0xFF;

	PORTB = 0x00;
	PORTG = 0xFF;

	digit0 = 0;
	digit1 = 0;
	digit2 = 0;
	digit3 = 0;
	number = 0;


	while(number < 1000)
	{
		digit0 = number % 10;
		digit1 = (number / 10) % 10;
		digit2 = (number / 100) % 10;

		for(int i = 0; i < 25; i++)
		{
			PORTG |= 0x0F;
			PORTG &= ~FND_C3;
			PORTG = Font[digit0];
			delay(100);

			PORTG |= 0x0F;
			PORTG &= ~FND_C2;
			PORTG = Font[digit1];
			delay(100);

			PORTG |= 0x0F;
			PORTG &= ~FND_C1;
			PORTG = Font[digit2];
			delay(100);

			PORTG |= 0x0F;
			PORTG &= ~FND_C0;
			PORTG = Font[digit3];
			delay(100);
		}

		number += 1;
	}

}
