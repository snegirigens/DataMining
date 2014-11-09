import MapReduce
import sys
import math

"""
Word Count Example in the Simple Python MapReduce Framework
"""

mr = MapReduce.MapReduce()

def isPrime (n):
    if n % 2 == 0 and n > 2:
        return False
    for i in range (3, int (math.sqrt(n)) + 1, 2):
        if n % i == 0:
            return False
    return True

def mapper (key):
    for i in range (2, int (key / 2) + 1):
        if isPrime (i) and key % i == 0:
            mr.emit_intermediate (i, key)

def reducer(key, list_of_values):
    total = 0
    for v in list_of_values:
      total += v
    mr.emit((key, total))

if __name__ == '__main__':
    # inputdata = [20];
    inputdata = [15, 21, 24, 30, 49]
    mr.execute(inputdata, mapper, reducer)
