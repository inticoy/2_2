#include <avr/io.h>

int main(void)
{
	unsigned char input_data;

	DDRD = 0x00;
	DDRB = 0xFF;

	while(1)
	{
		input_data = 0x00000001 << PIND;
		PORTB = input_data;
	}


}
