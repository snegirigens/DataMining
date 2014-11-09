import json

class MapReduce:
    def __init__(self):
        self.intermediate = {}
        self.result = []

    def emit_intermediate (self, key, value):
        self.intermediate.setdefault(key, [])
        self.intermediate[key].append(value)

    def emit (self, value):
        self.result.append(value) 

    def execute (self, data, mapper, reducer):
        for item in data:
            mapper (item)

        for key in self.intermediate:
            reducer (key, self.intermediate[key])

        for item in self.result:
            print (item)
