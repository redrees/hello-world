print("Hello World!")

N = 25

# Accepts a list of queens' positions
# Returns h(q)
def computeH(q):
    i = 0
    h = 0

    # Compute current h = number of pairs of queens attacking each other
    # Note that since the board starts with queens in distinct columns and we only move one at a time within the same column, we don't have to check whether a pair of queens occupy the same column
    while (i < N-1):
        j = i + 1
        while (j < N):
            # Is this pair of queens are on a same row?
            if (q[i] == q[j]):
                h += 1
            # Is this pair of queens are on a same diagonal?
            elif (abs(i - j) == abs(q[i] - q[j])):
                h += 1
            j += 1
        i += 1
    
    return h

def hillClimb(q):
    currenth = computeH(q)
    minh = currenth
    i = 0

    while (i < N):
        j = 1
        neighbor = list(q)
        while (j < N-1):
            neighbor[i] = (q[i] + j) % N
            neighborh = computeH(neighbor)

            if (neighborh < minh):
                minh = neighborh
                minNeighbor = list(neighbor)
            j += 1
        i += 1
    
    if (minh < currenth):
        return minNeighbor
    else:
        return None


current = []
board = [[0] * N for i in range(N)]

i = 0

while (i < N):
    current.append(i)
    i += 1

nextl = hillClimb(current)
while (nextl is not None):
    current = nextl
    nextl = hillClimb(current)

for i in range(0, N):
    board[current[i]][i] = 1

print(current)
print(computeH(current))
print()
for b in board:
    print(b)