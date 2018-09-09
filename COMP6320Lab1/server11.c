#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#define MAXLINE 4096 /*max text line length*/
#define SERV_PORT 10010  /*port*/
#define LISTENQ 8 /*maximum number of client connections*/

int main()
{
	int listenfd, connfd, n;
	pid_t childpid;
	socklen_t clilen;

	return 0;
}
