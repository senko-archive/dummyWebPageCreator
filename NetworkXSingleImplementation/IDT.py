
class IDT:
    def __init__(self, nodeName):
            #           IDT data structure
            #           [
            #               {bordernode : A,
            #               distance: 20,
            #               shortestPath = [S, x,y,c,A]},
            #               {bordernode : B,
            #               distance : 30,
            #               shortestPath = [blabla]}
            #            ]
        self.idtList = []
        self.nodeName = nodeName

