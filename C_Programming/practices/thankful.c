#include <stdio.h>
void tdif(char *t, char *g, char *i, char *f);
int main()
{
  char t[] = "Thank";
  char g[] = "God";
  char i[] = "it's";
  char f[] = "Friday";

  tdif(t, g, i, f);
  return 0;  
}
void tdif(char *t, char *g, char *i, char *f)
{
  printf("%s %s %s %s!\n", t, g, i, f);
}