# author: Hugh Kwon
# date: Sept. 12, 2018

import heapq

class Board:
    def __init__(self, board, g, predecessor):
        self.board = board
        self.g = g
        self.predecessor = predecessor
        self.__computeH()
        self.__computeF()

    def __computeH(self):
        self.h = 0
        for i in range(len(self.board)):
            if (self.board[i] > 0):
                self.h += abs(((self.board[i]-1)%5) - (i%5)) + abs(self.board[i] - i - 1)//5
            elif (self.board[i] == 0):
                self.z = i

    def __computeF(self):
        self.f = self.g + self.h

    def __up(self):
        if (self.z >= 20):
            return None
        if (self.board[self.z+5] == -1):
            return None

        newBoard = list(self.board)
        newBoard[self.z] = newBoard[self.z + 5]
        newBoard[self.z + 5] = 0

        return Board(newBoard, self.g + 1, self)

    def __down(self):
        if (self.z < 5):
            return None
        if (self.board[self.z-5] == -1):
            return None

        newBoard = list(self.board)
        newBoard[self.z] = newBoard[self.z - 5]
        newBoard[self.z - 5] = 0

        return Board(newBoard, self.g + 1, self)

    def __left(self):
        if (self.z%5 == 4):
            return None
        if (self.board[self.z + 1] == -1):
            return None

        newBoard = list(self.board)
        newBoard[self.z] = newBoard[self.z + 1]
        newBoard[self.z + 1] = 0

        return Board(newBoard, self.g + 1, self)

    def __right(self):
        if (self.z%5 == 0):
            return None
        if (self.board[self.z - 1] == -1):
            return None

        newBoard = list(self.board)
        newBoard[self.z] = newBoard[self.z - 1]
        newBoard[self.z - 1] = 0

        return Board(newBoard, self.g + 1, self)

    def getSuccessors(self):
        successors = []

        up = self.__up()
        down = self.__down()
        left = self.__left()
        right = self.__right()

        if (up is not None):
            successors.append(up)
        if (down is not None):
            successors.append(down)
        if (left is not None):
            successors.append(left)
        if (right is not None):
            successors.append(right)

        return successors

    def isGoalState(self):
        if (self.h != 0):
            return False
        else:
            return True

    def printBoard(self):
        s = '|'

        for i in range(len(self.board)):
            if (i%5 == 0 and i != 0):
                s += ' |'
                print(s)
                s = '|'

            s += ' ' + str(self.board[i])
            
        s += ' |'
        print(s)

    # equality comparison by board: needs to be worked
    def __eq__(self, other):
        if isinstance(other, self.__class__):
            return self.board == other.board
        else:
            return False

    def __ne__(self, other):
        return not self.__eq__(other)

    def __lt__(self, other):
        if isinstance(other, self.__class__):
            return self.f < other.f
        else:
            return False
    
    def __le__(self, other):
        if isinstance(other, self.__class__):
            return self.f <= other.f
        else:
            return False

    def __gt__(self, other):
        if isinstance(other, self.__class__):
            return self.f > other.f
        else:
            return False

    def __ge__(self, other):
        if isinstance(other, self.__class__):
            return self.f >= other.f
        else:
            return False


# Transform a list for easier manipulation
def transformA(l):
    for i in range(len(l)):
        if (l[i] > 14):
            l[i] += 1
        if (l[i] > 13):
            l[i] += 1
        if (l[i] > 10):
            l[i] += 1
        if (l[i] > 7):
            l[i] += 1
        if (l[i] > 6):
            l[i] += 1

# Transform a list to displayable format
def transformB(l):
    for i in range(len(l)):
        if (l[i] > 19):
            l[i] -= 1
        if (l[i] > 17):
            l[i] -= 1
        if (l[i] > 13):
            l[i] -= 1
        if (l[i] > 9):
            l[i] -= 1
        if (l[i] > 7):
            l[i] -= 1

def isNewPathBetter(list, board):
    for element in list:
        if element == list and list < element:
            return True

    return False


initial = [2, 3, 7, 4, 5,
           1, -1, 11, -1, 8,
           6, 10, 0, 12, 15,
           9, -1, 14, -1, 20,
           13, 16, 17, 18, 19 ]

transformA(initial)

s = Board(initial, 0, None)

openPQ = []
closed = []
path = []
heapq.heappush(openPQ, s)
goalFound = False

# A* algorithm to find a solution path
while len(openPQ) > 0 and goalFound == False:
    current = heapq.heappop(openPQ)

    if current.isGoalState():
        path.append(current)
        n = current.predecessor
        goalFound = True
        while n is not None:
            path.append(n)
            n = n.predecessor
    else:
        nexts = current.getSuccessors()

        for n in nexts:
            if n in openPQ:
                if isNewPathBetter(openPQ, n):
                    openPQ.remove(n)
                    heapq.heappush(openPQ, n)
            elif n in closed:
                if isNewPathBetter(closed, n):
                    closed.remove(n)
                    heapq.heappush(openPQ, n)
            else: # n is in neither openPQ or closed
                heapq.heappush(openPQ, n)

path.reverse()

print(str(len(path)) + 'steps taken')
count = 0

for n in path:
    count += 1
    print(str(count) + 'th state')
    transformB(n.board)
    n.printBoard()