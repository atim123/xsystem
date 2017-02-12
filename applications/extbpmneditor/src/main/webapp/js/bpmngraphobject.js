
var XS = XS || {};
XS.graphObject = {
    $: go.GraphObject.make,
    swimlanes: {
        MINLENGTH: 400, // this controls the minimum length of any swimlane
        MINBREADTH: 20  // this controls the minimum breadth of any non-collapsed swimlane

    }

};


// --------------- bpmngroup__--------------------------------------------------
XS.graphObject.bpmngroup = {
    task: "task",
    gateway: "gateway",
    event: "event",
    sequence: "",
    msg: "msg",
    pool: "pool",
    lane: "lane"
};


XS.graphObject.bpmntype = {
    StartEvent: "StartEvent", //Start", 
    EndEvent: "EndEvent",
    TerminateEvent: "TerminateEvent",
    TimerEvent: "TimerEvent",
    BoundaryTimerEvent: "BoundaryTimerEvent",
    BoundaryMessageEvent: "BoundaryMessageEvent",
    CatchMessageEvent: "CatchMessageEvent",
    ThrowMessageEvent: "ThrowMessageEvent",
    UserTask: "UserTask",
    ServiceTask: "ServiceTask",
    ScriptTask: "ScriptTask",
    ReceiveTask: "ReceiveTask",
    SendTask: "SendTask",
    ParallelGateway: "ParallelGateway",
    ExclusiveGateway: "ExclusiveGateway",
    EventGateway: "EventGateway",
    Process: "Process",
    Lane: "Lane",
    MessageFlow: "MessageFlow",
    SequenceFlow: "SequenceFlow"
};

//---------Common---------------------------------------------------

XS.graphObject.groupStyle = function () {  // common settings for both Lane and Pool Groups
    return [
        {
            layerName: "Background", // all pools and lanes are always behind all nodes and links
            background: "transparent", // can grab anywhere in bounds
            movable: true, // allows users to re-order by dragging
            copyable: false, // can't copy lanes or pools
            avoidable: false  // don't impede AvoidsNodes routed Links
        },
        new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify)
    ];
}

XS.graphObject.computeMinPoolSize = function (pool) {
    //var MINLENGTH = 400;
    // assert(pool instanceof go.Group && pool.category === "Pool");
    var len = XS.graphObject.swimlanes.MINLENGTH;
    pool.memberParts.each(function (lane) {
        // pools ought to only contain lanes, not plain Nodes
        if (!(lane instanceof go.Group))
            return;
        var holder = lane.placeholder;
        if (holder !== null) {
            var sz = holder.actualBounds;
            len = Math.max(len, sz.width);
        }
    });
    return new go.Size(len, NaN);
};

XS.graphObject.computeLaneSize = function (lane) {
    // assert(lane instanceof go.Group && lane.category !== "Pool");
    var sz = XS.graphObject.computeMinLaneSize(lane);
    if (lane.isSubGraphExpanded) {
        var holder = lane.placeholder;
        if (holder !== null) {
            var hsz = holder.actualBounds;
            sz.height = Math.max(sz.height, hsz.height);
        }
    }
    // minimum breadth needs to be big enough to hold the header
    var hdr = lane.findObject("HEADER");
    if (hdr !== null)
        sz.height = Math.max(sz.height, hdr.actualBounds.height);
    return sz;
}

XS.graphObject.computeMinLaneSize = function (lane) {
    var MINLENGTH = XS.graphObject.swimlanes.MINLENGTH;  // this controls the minimum length of any swimlane
    var MINBREADTH = XS.graphObject.swimlanes.MINBREADTH;  // this controls the minimum breadth of any non-collapsed swimlane

    if (!lane.isSubGraphExpanded)
        return new go.Size(MINLENGTH, 1);
    return new go.Size(MINLENGTH, MINBREADTH);
}

XS.graphObject.relayoutDiagram = function (myDiagram) {

    myDiagram.layout.invalidateLayout();
    myDiagram.findTopLevelGroups().each(function (g) {
        if (g.category === "pool")
            g.layout.invalidateLayout();
    });
    myDiagram.layoutDiagram();
};

//-----------BPMNLinkingTool----------------------------------------------------
XS.graphObject.BPMNLinkingTool = function () {
    go.LinkingTool.call(this);
    this.linkValidation = function (fromnode, fromport, tonode, toport) {
        return XS.graphObject.BPMNLinkingTool.validateSequenceLinkConnection(fromnode, fromport, tonode, toport) ||
                XS.graphObject.BPMNLinkingTool.validateMessageLinkConnection(fromnode, fromport, tonode, toport);
    };
};

go.Diagram.inherit(XS.graphObject.BPMNLinkingTool, go.LinkingTool);

XS.graphObject.BPMNLinkingTool.prototype.insertLink = function (fromnode, fromport, tonode, toport) {
    /*
     var newlink = go.LinkingTool.prototype.insertLink.call(this, fromnode, fromport, tonode, toport);
     if (newlink.category === XS.graphObject.bpmngroup.sequence) {
     newlink.data.type = XS.graphObject.bpmntype.SequenceFlow;
     }
     return newlink;
     */

    var lsave = null;
    // maybe temporarily change the link data that is copied to create the new link
    if (XS.graphObject.BPMNLinkingTool.validateMessageLinkConnection(fromnode, fromport, tonode, toport)) {
        lsave = this.archetypeLinkData;
        var fromModelData = fromnode.data;
        var toModelData = tonode.data;

        if (!fromModelData.message && toModelData.message) {
            fromModelData.message = toModelData.message;

        } else {
            toModelData.message = fromModelData.message;
        }


        this.archetypeLinkData = {category: "msg", "type": "MessageFlow", text: toModelData.message, message: toModelData.message};
    }

    // create the link in the standard manner by calling the base method
    var newlink = go.LinkingTool.prototype.insertLink.call(this, fromnode, fromport, tonode, toport);

    // maybe make the label visible
    if (fromnode.category === "gateway") {
        var label = newlink.findObject("Label");
        if (label !== null)
            label.visible = true;
    }

    // maybe restore the original archetype link data
    if (lsave !== null)
        this.archetypeLinkData = lsave;

    if (newlink.category === XS.graphObject.bpmngroup.sequence) {
        newlink.data.type = XS.graphObject.bpmntype.SequenceFlow;
    }
    return newlink;
};


// in BPMN, can't link sequence flows across subprocess or pool boundaries
XS.graphObject.BPMNLinkingTool.validateSequenceLinkConnection = function (fromnode, fromport, tonode, toport) {
    if (fromnode.category === null || tonode.category === null)
        return true;

    // if either node is in a subprocess, both nodes must be in same subprocess (not even Message Flows) 
    if ((fromnode.containingGroup !== null && fromnode.containingGroup.category === "subprocess") ||
            (tonode.containingGroup !== null && tonode.containingGroup.category === "subprocess")) {
        if (fromnode.containingGroup !== tonode.containingGroup)
            return false;
    }

    if (fromnode.containingGroup === tonode.containingGroup)
        return true;  // a valid Sequence Flow
    // also check for children in common pool
    var common = fromnode.findCommonContainingGroup(tonode);
    return common != null;
};

// in BPMN, Message Links must cross pool boundaries
XS.graphObject.BPMNLinkingTool.validateMessageLinkConnection = function (fromnode, fromport, tonode, toport) {
    if (!tonode) {
        return false;
    }

    if (fromnode.category === null || tonode.category === null)
        return true;

    if (fromnode.category === "privateProcess" || tonode.category === "privateProcess")
        return true;

    // if either node is in a subprocess, both nodes must be in same subprocess (not even Message Flows) 
    if ((fromnode.containingGroup !== null && fromnode.containingGroup.category === "subprocess") ||
            (tonode.containingGroup !== null && tonode.containingGroup.category === "subprocess")) {
        if (fromnode.containingGroup !== tonode.containingGroup)
            return false;
    }

    if (fromnode.containingGroup === tonode.containingGroup)
        return false;  // an invalid Message Flow
    // also check for children in common pool
    var common = fromnode.findCommonContainingGroup(tonode);
    return common === null;
};


//-----------PoolLayout---------------------------------------------------------
XS.graphObject.PoolLayout = function () {
    go.GridLayout.call(this);
    this.cellSize = new go.Size(1, 1);
    this.wrappingColumn = 1;
    this.wrappingWidth = Infinity;
    this.isRealtime = false;  // don't continuously layout while dragging
    this.alignment = go.GridLayout.Position;
    // This sorts based on the location of each Group.
    // This is useful when Groups can be moved up and down in order to change their order.
    this.comparer = function (a, b) {
        var ay = a.location.y;
        var by = b.location.y;
        if (isNaN(ay) || isNaN(by))
            return 0;
        if (ay < by)
            return -1;
        if (ay > by)
            return 1;
        return 0;
    };
};




go.Diagram.inherit(XS.graphObject.PoolLayout, go.GridLayout);

/** @override */
XS.graphObject.PoolLayout.prototype.doLayout = function (coll) {
    var diagram = this.diagram;
    if (diagram === null)
        return;
    diagram.startTransaction("PoolLayout");
    var pool = this.group;
    if (pool !== null && pool.category === "pool") {
        // make sure all of the Group Shapes are big enough
        var minsize = XS.graphObject.computeMinPoolSize(pool);
        pool.memberParts.each(function (lane) {
            if (!(lane instanceof go.Group))
                return;
            if (lane.category !== "pool") {
                var shape = lane.resizeObject;
                if (shape !== null) {  // change the desiredSize to be big enough in both directions
                    var sz = XS.graphObject.computeLaneSize(lane);
                    shape.width = (isNaN(shape.width) ? minsize.width : Math.max(shape.width, minsize.width));
                    shape.height = (!isNaN(shape.height)) ? Math.max(shape.height, sz.height) : sz.height;
                    var cell = lane.resizeCellSize;
                    if (!isNaN(shape.width) && !isNaN(cell.width) && cell.width > 0)
                        shape.width = Math.ceil(shape.width / cell.width) * cell.width;
                    if (!isNaN(shape.height) && !isNaN(cell.height) && cell.height > 0)
                        shape.height = Math.ceil(shape.height / cell.height) * cell.height;
                }
            }
        });
    }
    // now do all of the usual stuff, according to whatever properties have been set on this GridLayout
    go.GridLayout.prototype.doLayout.call(this, coll);
    diagram.commitTransaction("PoolLayout");
};


//-----------------------LaneResizingTool---------------------------------------            
// define a custom ResizingTool to limit how far one can shrink a lane Group
// XS.graphObject.            
XS.graphObject.LaneResizingTool = function () {
    go.ResizingTool.call(this);
}
;
go.Diagram.inherit(XS.graphObject.LaneResizingTool, go.ResizingTool);

XS.graphObject.LaneResizingTool.prototype.isLengthening = function () {
    return (this.handle.alignment === go.Spot.Right);
};


XS.graphObject.LaneResizingTool.prototype.computeMinSize = function () {
    var lane = this.adornedObject.part;
    // assert(lane instanceof go.Group && lane.category !== "Pool");
    var msz = XS.graphObject.computeMinLaneSize(lane);  // get the absolute minimum size
    if (this.isLengthening()) {  // compute the minimum length of all lanes
        var sz = XS.graphObject.computeMinPoolSize(lane.containingGroup);
        msz.width = Math.max(msz.width, sz.width);
    } else {  // find the minimum size of this single lane
        var sz = XS.graphObject.computeLaneSize(lane);
        msz.width = Math.max(msz.width, sz.width);
        msz.height = Math.max(msz.height, sz.height);
    }
    return msz;
};

XS.graphObject.LaneResizingTool.prototype.canStart = function () {
    if (!go.ResizingTool.prototype.canStart.call(this))
        return false;

    // if this is a resize handle for a "Lane", we can start.
    var diagram = this.diagram;
    if (diagram === null)
        return;
    var handl = this.findToolHandleAt(diagram.firstInput.documentPoint, this.name);
    if (handl === null || handl.part === null || handl.part.adornedObject === null || handl.part.adornedObject.part === null)
        return false;
    return (handl.part.adornedObject.part.category === "lane");
}

XS.graphObject.LaneResizingTool.prototype.resize = function (newr) {
    var lane = this.adornedObject.part;
    if (this.isLengthening()) {  // changing the length of all of the lanes
        lane.containingGroup.memberParts.each(function (lane) {
            if (!(lane instanceof go.Group))
                return;
            var shape = lane.resizeObject;
            if (shape !== null) {  // set its desiredSize length, but leave each breadth alone
                shape.width = newr.width;
            }
        });
    } else {  // changing the breadth of a single lane
        go.ResizingTool.prototype.resize.call(this, newr);
    }
    var myDiagram = this.diagram;
    myDiagram.layout.invalidateLayout();
    myDiagram.findTopLevelGroups().each(function (g) {
        if (g.category === "pool")
            g.layout.invalidateLayout();
    });
    myDiagram.layoutDiagram();
    // relayoutDiagram();  // now that the lane has changed size, layout the pool again
};

//-----------------------PoolLink-----------------------------------------------
XS.graphObject.PoolLink = function PoolLink() {
    go.Link.call(this);
}
go.Diagram.inherit(XS.graphObject.PoolLink, go.Link);


XS.graphObject.PoolLink.prototype.getLinkPoint = function (node, port, spot, from, ortho, othernode, otherport) {
    var r = new go.Rect(port.getDocumentPoint(go.Spot.TopLeft),
            port.getDocumentPoint(go.Spot.BottomRight));
    var op = go.Link.prototype.getLinkPoint.call(this, othernode, otherport, spot, from, ortho, node, port);

    var below = op.y > r.centerY;
    var y = below ? r.bottom : r.top;
    if (node.category === "privateProcess") {
        if (op.x < r.left)
            return new go.Point(r.left, y);
        if (op.x > r.right)
            return new go.Point(r.right, y);
        return new go.Point(op.x, y);
    } else { // otherwise get the standard link point by calling the base class method
        return go.Link.prototype.getLinkPoint.call(this, node, port, spot, from, ortho, othernode, otherport);
    }
};



// If there are two links from & to same node... and pool is offset in X from node... the link toPoints collide on pool

XS.graphObject.PoolLink.prototype.computeOtherPoint = function (othernode, otherport) {
    var op = go.Link.prototype.computeOtherPoint(this, othernode, otherport);
    var node = this.toNode;
    if (node === othernode)
        node = this.fromNode;
    if (othernode.category === "privateProcess") {
        op.x = node.getDocumentPoint(go.Spot.MiddleBottom).x;
    }
    return op;
};



//------------------------------------PoolGroupTemplate-------------------------
XS.graphObject.PoolGroupTemplate = function () {
    var $ = XS.graphObject.$;

    var GatewayNodeSize = 80;

    this.getDiagramTemplate = function () {
        if (!this.diagramTemplate) {
            this.diagramTemplate = $(go.Group, "Auto", XS.graphObject.groupStyle(),
                    {// use a simple layout that PoolLayout ignores links to stack the "lane" Groups on top of each other
                        layout: $(XS.graphObject.PoolLayout, {spacing: new go.Size(0, 0)})  // no space between lanes
                    },
                    new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                    new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
                    $(go.Shape,
                            {fill: "white"},
                            new go.Binding("fill", "color")

                            ),
                    $(go.Panel, "Table",
                            {defaultColumnSeparatorStroke: "black"},
                            $(go.Panel, "Horizontal",
                                    {column: 0, angle: 270},
                                    $(go.TextBlock,
                                            {editable: true, margin: new go.Margin(5, 0, 5, 0)}, // margin matches private process (black box pool)
                                            new go.Binding("text").makeTwoWay())
                                    ),
                            $(go.Placeholder,
                                    {background: "darkgray", column: 1})
                            )
                    );
        }
        return this.diagramTemplate;
    };
    this.getPaletteTemplate = function () {
        if (!this.paletteTemplate) {
            this.paletteTemplate =
                    $(go.Group, "Vertical",
                            {locationSpot: go.Spot.Center,
                                isSubGraphExpanded: false
                            },
                            $(go.Shape, "Process",
                                    {fill: "white", desiredSize: new go.Size(GatewayNodeSize / 2, GatewayNodeSize / 4)}),
                            $(go.Shape, "Process",
                                    {fill: "white", desiredSize: new go.Size(GatewayNodeSize / 2, GatewayNodeSize / 4)}),
                            $(go.TextBlock,
                                    {margin: 5, editable: true},
                                    new go.Binding("text"))
                            );
        }
        return this.paletteTemplate;
    };
};


XS.graphObject.SwimLanesGroupTemplate = function () {
    var self = this;
    var $ = XS.graphObject.$;


    var laneEventMenu =
            $(go.Adornment, "Vertical",
                    $("ContextMenuButton",
                            $(go.TextBlock, "Add Lane"),
                            {click: function (e, obj) {
                                    self.addLaneEvent(obj.part.adornedObject);
                                }})
                    );

    var layout = $(go.LayeredDigraphLayout, // automatically lay out the lane's subgraph
            {
                isInitial: false, // don't even do initial layout
                isOngoing: false, // don't invalidate layout when nodes or links are added or removed
                direction: 0,
                columnSpacing: 10,
                layeringOption: go.LayeredDigraphLayout.LayerLongestPathSource
            });

    this.updateCrossLaneLinks = function (group) {
        group.findExternalLinksConnected().each(function (l) {
            l.visible = (l.fromNode.isVisible() && l.toNode.isVisible());
        });
    };

    this.addLaneEvent = function (lane) {
        var MINBREADTH = XS.graphObject.swimlanes.MINBREADTH;
        var myDiagram = lane.diagram;
        myDiagram.startTransaction("addLane");
        if (lane != null && lane.data.category === "lane") {
            // create a new lane data object
            var shape = lane.findObject("SHAPE");
            var size = new go.Size(shape.width, MINBREADTH);
            //size.height = MINBREADTH;
            var newlanedata = {
                category: "lane",
                text: "New Lane",
                color: "white",
                type: "Lane",
                isGroup: true,
                loc: go.Point.stringify(new go.Point(lane.location.x, lane.location.y + 1)), // place below selection
                size: go.Size.stringify(size),
                group: lane.data.group
            };
            // and add it to the model
            myDiagram.model.addNodeData(newlanedata);
        }
        myDiagram.commitTransaction("addLane");
    };


    this.mouseDrop = function (e, grp) {  // dropping a copy of some Nodes and Links onto this Group adds them to this Group
        // don't allow drag-and-dropping a mix of regular Nodes and Groups
        if (!e.diagram.selection.any(function (n) {
            return (n instanceof go.Group && n.category !== "subprocess") || n.category === "privateProcess";
        })) {
            var ok = grp.addMembers(grp.diagram.selection, true);
            if (ok) {
                self.updateCrossLaneLinks(grp);
                XS.graphObject.relayoutDiagram(e.diagram);
            } else {
                grp.diagram.currentTool.doCancel();
            }
        }
    };

    this.subGraphExpandedChanged = function (grp) {
        var shp = grp.resizeObject;
        if (grp.diagram.undoManager.isUndoingRedoing)
            return;
        if (grp.isSubGraphExpanded) {
            shp.height = grp._savedBreadth;
        } else {
            grp._savedBreadth = shp.height;
            shp.height = NaN;
        }
        self.updateCrossLaneLinks(grp);
    }
    ;



    this.getDiagramTemplate = function () {
        if (!this.diagramTemplate) {
            var ret = $(go.Group, "Spot", XS.graphObject.groupStyle(),
                    new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                    {
                        name: "Lane",
                        contextMenu: laneEventMenu,
                        minLocation: new go.Point(NaN, -Infinity), // only allow vertical movement
                        maxLocation: new go.Point(NaN, Infinity),
                        selectionObjectName: "SHAPE", // selecting a lane causes the body of the lane to be highlit, not the label
                        resizable: true, resizeObjectName: "SHAPE", // the custom resizeAdornmentTemplate only permits two kinds of resizing
                        layout: layout,
                        computesBoundsAfterDrag: true, // needed to prevent recomputing Group.placeholder bounds too soon
                        computesBoundsIncludingLinks: false, // to reduce occurrences of links going briefly outside the lane
                        computesBoundsIncludingLocation: true, // to support empty space at top-left corner of lane
                        handlesDragDropForMembers: true, // don't need to define handlers on member Nodes and Links
                        mouseDrop: self.mouseDrop,
                        subGraphExpandedChanged: self.subGraphExpandedChanged
                    },
            //new go.Binding("isSubGraphExpanded", "expanded").makeTwoWay(),

                    $(go.Shape, "Rectangle", // this is the resized object
                            {name: "SHAPE", fill: "white", stroke: null}, // need stroke null here or you gray out some of pool border.
                            new go.Binding("fill", "color"),
                            new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify)),
                    // the lane header consisting of a Shape and a TextBlock
                    $(go.Panel, "Horizontal",
                            {
                                name: "HEADER",
                                angle: 270, // maybe rotate the header to read sideways going up
                                alignment: go.Spot.LeftCenter, alignmentFocus: go.Spot.LeftCenter
                            },
                            $(go.TextBlock, // the lane label
                                    {editable: true, margin: new go.Margin(2, 0, 0, 8)},
                                    new go.Binding("visible", "isSubGraphExpanded").ofObject(),
                                    new go.Binding("text", "text").makeTwoWay()),
                            $("SubGraphExpanderButton", {margin: 4, angle: -270})  // but this remains always visible!
                            ), // end Horizontal Panel
                    $(go.Placeholder,
                            {padding: 12, alignment: go.Spot.TopLeft, alignmentFocus: go.Spot.TopLeft}),
                    $(go.Panel, "Horizontal", {alignment: go.Spot.TopLeft, alignmentFocus: go.Spot.TopLeft},
                            $(go.TextBlock, // this TextBlock is only seen when the swimlane is collapsed
                                    {
                                        name: "LABEL",
                                        editable: true, visible: false,
                                        angle: 0, margin: new go.Margin(6, 0, 0, 20)
                                    },
                                    new go.Binding("visible", "isSubGraphExpanded", function (e) {
                                        return !e;
                                    }).ofObject(),
                                    new go.Binding("text", "text").makeTwoWay())
                            )
                    );  // end swimLanesGroupTemplate

            ret.resizeAdornmentTemplate =
                    $(go.Adornment, "Spot",
                            $(go.Placeholder),
                            $(go.Shape, // for changing the length of a lane
                                    {
                                        alignment: go.Spot.Right,
                                        desiredSize: new go.Size(7, 50),
                                        fill: "lightblue", stroke: "dodgerblue",
                                        cursor: "col-resize"
                                    },
                                    new go.Binding("visible", "", function (ad) {
                                        return ad.adornedPart.isSubGraphExpanded;
                                    }).ofObject()),
                            $(go.Shape, // for changing the breadth of a lane
                                    {
                                        alignment: go.Spot.Bottom,
                                        desiredSize: new go.Size(50, 7),
                                        fill: "lightblue", stroke: "dodgerblue",
                                        cursor: "row-resize"
                                    },
                                    new go.Binding("visible", "", function (ad) {
                                        return ad.adornedPart.isSubGraphExpanded;
                                    }).ofObject())
                            );
            this.diagramTemplate = ret;
        }

        return this.diagramTemplate;
    };

    this.getPaletteTemplate = function () {
        return $(go.Group, "Vertical"); // empty in the palette
    };

};


XS.graphObject.addActivityNodeBoundaryEvent = function (e, evType) {
    var diagram = e.diagram;


    diagram.startTransaction("addBoundaryEvent");
    diagram.selection.each(function (node) {
        // skip any selected Links
        if (!(node instanceof go.Node))
            return;
        if (node.data && (node.data.category === XS.graphObject.bpmngroup.task || node.data.category === "subprocess")) {
            // compute the next available index number for the side
            var i = 0;
            if (!node.data.boundaryEventArray) {
                diagram.model.setDataProperty(node.data, "boundaryEventArray", []);
            } else {
                i = node.data.boundaryEventArray.length;
            }
            var name = diagram.model.makeUniqueKeyFunction(diagram.model);
            /*      
             var i = 0;
             var defaultPort = node.findPort("");
             while (node.findPort("be" + i.toString()) !== defaultPort)
             i++;           // now this new port name is unique within the whole Node because of the side prefix
             var name = "be" + i.toString();
             if (!node.data.boundaryEventArray) {
             diagram.model.setDataProperty(node.data, "boundaryEventArray", []);
             }       // initialize the Array of port data if necessary
             // 
             // 
             // 
             // create a new port data object
             // .makeUniqueKeyFunction
             */
            var newportdata = {
                portId: name,
                type: evType,
                eventDimension: 6,
                color: "white",
                alignmentIndex: i
                        // if you add port data properties here, you should copy them in copyPortData above  ** BUG...  we don't do that.
            };
            // and add it to the Array of port data
            diagram.model.insertArrayItem(node.data.boundaryEventArray, -1, newportdata);
        }
    });
    diagram.commitTransaction("addBoundaryEvent");


};

XS.graphObject.getActivityNodeMenu = function () {
    var $ = XS.graphObject.$;
    var ret = $(go.Adornment, "Vertical",
            /* $("ContextMenuButton",
             $(go.TextBlock, "Add message Event", {margin: 3}),
             {click: function (e, obj) {
             XS.graphObject.addActivityNodeBoundaryEvent(e, XS.graphObject.bpmntype.BoundaryMessageEvent);
             }}),*/
            $("ContextMenuButton",
                    $(go.TextBlock, "Add Timer Event", {margin: 3}),
                    {click: function (e, obj) {
                            XS.graphObject.addActivityNodeBoundaryEvent(e, XS.graphObject.bpmntype.BoundaryTimerEvent);
                        }})
            );
    return ret;
};

XS.graphObject.getBoundaryEventMenu = function () {
    var $ = XS.graphObject.$;
    var ret = $(go.Adornment, "Vertical",
            $("ContextMenuButton",
                    $(go.TextBlock, "Remove event"),
                    // in the click event handler, the obj.part is the Adornment; its adornedObject is the port
                            {click: function (e, obj) {
                                    removeActivityNodeBoundaryEvent(e, obj.part.adornedObject);
                                }})
                            );

                    // removing a boundary event doesn't not reposition other BE circles on the node
                    // just reassigning alignmentIndex in remaining BE would do that.
                    function removeActivityNodeBoundaryEvent(e, obj) {
                        var diagram = e.diagram;
                        diagram.startTransaction("removeBoundaryEvent");
                        var pid = obj.portId;
                        var arr = obj.panel.itemArray;
                        for (var i = 0; i < arr.length; i++) {
                            if (arr[i].portId === pid) {
                                diagram.model.removeArrayItem(arr, i);
                                break;
                            }
                        }
                        diagram.commitTransaction("removeBoundaryEvent");
                    }
                    ;

                    return ret;
                };

        XS.graphObject.getBoundaryEventItemTemplate = function () {
            var $ = XS.graphObject.$;
            var ret = $(go.Panel, "Spot",
                    {contextMenu: XS.graphObject.getBoundaryEventMenu(),
                        alignmentFocus: go.Spot.Center,
                        fromLinkable: true, toLinkable: false, cursor: "pointer", fromSpot: go.Spot.Bottom,
                        fromMaxLinks: 1, toMaxLinks: 0,
                    },
                    new go.Binding("portId", "portId"),
                    new go.Binding("alignment", "alignmentIndex",
                            function (s) {
                                var x = 31;
                                if (s === 0)
                                    return new go.Spot(0, 1, x, 0);    // bottom left
                                if (s === 1)
                                    return new go.Spot(1, 1, -x, 0);   // bottom right
                                if (s === 2)
                                    return new go.Spot(1, 0, -x, 0);   // top right
                                return new go.Spot(1, 0, -x - (s - 2) * 42, 0);
                            }

                    ),
                    $(go.Shape, "Circle",
                            {desiredSize: new go.Size(42, 42)},
                    //    new go.Binding("strokeDashArray", "eventDimension", function (s) {
                    //        return  null;
                    //    }),
                            new go.Binding("fromSpot", "alignmentIndex",
                                    function (s) {
                                        if (s < 2)
                                            return go.Spot.Bottom;
                                        return go.Spot.Top;
                                    }),
                            new go.Binding("fill", "color")),
                    $(go.Shape, "Circle",
                            {alignment: go.Spot.Center,
                                desiredSize: new go.Size(36, 36), fill: null}
                    /* new go.Binding("strokeDashArray", "eventDimension", function (s) {
                     return  null;
                     })*/
                    ),
                    $(go.Shape, "NotAllowed",
                            {alignment: go.Spot.Center,
                                isActionable: true,
                                click: function (e, tb) {
                                    tb.diagram.model.portselectionCb(tb);
                                    /* console.log(tb.diagram.model.portselectionCb);
                                     console.log(tb.panel.data);
                                     console.log("--tb--"); 
                                     console.log(tb);  
                                     console.log("--e--");
                                     console.log(e);*/
                                },
                                desiredSize: new go.Size(22, 22), fill: "white"},
                            new go.Binding("figure", "type", function (type) {
                                if (type === XS.graphObject.bpmntype.BoundaryTimerEvent)
                                    return "BpmnEventTimer";
                                else if (type === XS.graphObject.bpmntype.BoundaryMessageEvent)
                                    return "BpmnTaskMessage"
                            })
                            )
                    );
            return ret;
        };

//----------------- TaskTemplate -----------------------------------------------   
        XS.graphObject.TaskTemplate = function () {
            var $ = XS.graphObject.$;
            var ActivityNodeFill = $(go.Brush, "Linear", {0: "OldLace", 1: "PapayaWhip"});
            var ActivityNodeStroke = "#CDAA7D";
            var ActivityMarkerStrokeWidth = 1.5;
            var ActivityNodeWidth = 120;
            var ActivityNodeHeight = 80;
            var ActivityNodeStrokeWidth = 1;
            var ActivityNodeStrokeWidthIsCall = 4;
            var figureSize = 22;

            var taskDrawProprty = {// TenPointedBurst AsteriskLine
                UserTask: {"figure": "BpmnTaskUser", "fill": "white"},
                ServiceTask: {"figure": "BpmnTaskService", "fill": "dimgray"},
                ScriptTask: {"figure": "BpmnTaskScript", "fill": "white"},
                ReceiveTask: {"figure": "BpmnTaskMessage", "fill": "white"},
                SendTask: {"figure": "BpmnTaskMessage", "fill": "dimgray"}
            };

            function makeFigure(isPalete) {
                var fSize = (isPalete) ? figureSize / 2 : figureSize;

                var ret =
                        $(go.Shape, // "BpmnTaskScript", // will be None, Script, Manual, Service, etc via converter
                                {alignment: new go.Spot(0, 0, 5, 5), alignmentFocus: go.Spot.TopLeft,
                                    width: fSize, height: fSize
                                },
                                new go.Binding("fill", "type", function (tpy) {
                                    return taskDrawProprty[tpy].fill;
                                }),
                                new go.Binding("figure", "type", function (tpy) {
                                    return  taskDrawProprty[tpy].figure;
                                })

                                );
                return ret;
            }

            function makeText(editable) {
                var ret = $(go.TextBlock,
                        {alignment: go.Spot.Center, textAlign: "center", margin: 12,
                            editable: editable,
                            text: "Task"
                        },
                        new go.Binding("text").makeTwoWay());
                return ret;
            }

            function makeMarkerPanel(sub, scale) {
                return $(go.Panel, "Horizontal",
                        {alignment: go.Spot.MiddleBottom, alignmentFocus: go.Spot.MiddleBottom},
                        $(go.Shape, "BpmnActivityLoop",
                                {width: 12 / scale, height: 12 / scale, margin: 2, visible: false, strokeWidth: ActivityMarkerStrokeWidth},
                                new go.Binding("visible", "isLoop")),
                        $(go.Shape, "BpmnActivityParallel",
                                {width: 12 / scale, height: 12 / scale, margin: 2, visible: false, strokeWidth: ActivityMarkerStrokeWidth},
                                new go.Binding("visible", "isParallel")),
                        $(go.Shape, "BpmnActivitySequential",
                                {width: 12 / scale, height: 12 / scale, margin: 2, visible: false, strokeWidth: ActivityMarkerStrokeWidth},
                                new go.Binding("visible", "isSequential")),
                        $(go.Shape, "BpmnActivityAdHoc",
                                {width: 12 / scale, height: 12 / scale, margin: 2, visible: false, strokeWidth: ActivityMarkerStrokeWidth},
                                new go.Binding("visible", "isAdHoc")),
                        $(go.Shape, "BpmnActivityCompensation",
                                {width: 12 / scale, height: 12 / scale, margin: 2, visible: false, strokeWidth: ActivityMarkerStrokeWidth, fill: null},
                                new go.Binding("visible", "isCompensation"))
                        // makeSubButton(sub)
                        ); // end activity markers horizontal panel
            }
            ;

            this.getDiagramTemplate = function () {
                if (!this.diagramTemplate) {
                    this.diagramTemplate =
                            $(go.Node, "Spot",
                                    {locationObjectName: "SHAPE",
                                        locationSpot: go.Spot.Center,
                                        resizable: true,
                                        toolTip: $(go.Adornment, go.Panel.Auto,
                                                $(go.Shape, "RoundedRectangle",
                                                        {fill: "whitesmoke", stroke: "gray"}),
                                                $(go.TextBlock,
                                                        {margin: 3, editable: true},
                                                        new go.Binding("text", "", function (data) {
                                                            if (data.item !== undefined)
                                                                return data.item;
                                                            return "(unnamed item)";
                                                        }))
                                                ),
                                        resizeObjectName: "PANEL",
                                        selectionAdorned: false,
                                        contextMenu: XS.graphObject.getActivityNodeMenu(),
                                        itemTemplate: XS.graphObject.getBoundaryEventItemTemplate()
                                    },
                                    new go.Binding("itemArray", "boundaryEventArray"),
                                    new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
                                    new go.Binding("layerName", "isSelected", function (s) {
                                        return s ? "Foreground" : "";
                                    }).ofObject(),
                                    $(go.Panel, "Auto",
                                            {name: "PANEL",
                                                minSize: new go.Size(ActivityNodeWidth, ActivityNodeHeight),
                                                desiredSize: new go.Size(ActivityNodeWidth, ActivityNodeHeight)
                                            },
                                            new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                                            $(go.Panel, "Spot",
                                                    $(go.Shape, "RoundedRectangle",
                                                            {
                                                                name: "SHAPE",
                                                                fill: ActivityNodeFill,
                                                                stroke: ActivityNodeStroke,
                                                                parameter1: 10,
                                                                portId: "",
                                                                fromLinkable: true,
                                                                toLinkable: true,
                                                                cursor: "pointer"
                                                            }

                                                    ),
                                                    makeFigure(),
                                                    makeMarkerPanel(false, 1)
                                                    ),
                                            makeText(true)
                                            )
                                    );
                }
                return this.diagramTemplate;
            };
            this.getPaletteTemplate = function () {
                if (!this.paletteTemplate) {
                    var palscale = 2;
                    this.paletteTemplate =
                            $(go.Node, "Vertical",
                                    {locationObjectName: "SHAPE",
                                        locationSpot: go.Spot.Center,
                                        selectionAdorned: false
                                    },
                                    $(go.Panel, "Spot",
                                            {name: "PANEL",
                                                desiredSize: new go.Size(ActivityNodeWidth / palscale, ActivityNodeHeight / palscale)
                                            },
                                            $(go.Shape, "RoundedRectangle", // the outside rounded rectangle
                                                    {name: "SHAPE",
                                                        fill: ActivityNodeFill,
                                                        stroke: ActivityNodeStroke,
                                                        parameter1: 10 / palscale  // corner size (default 10)
                                                    }
                                            ),
                                            makeFigure(true)

                                            ),
                                    makeText(false)
                                    );
                }
                return this.paletteTemplate;
            };
        };

//-----------------GatewayTemplate----------------------------------------------
        XS.graphObject.GatewayTemplate = function () {
            var $ = XS.graphObject.$;
            var GradientYellow = $(go.Brush, "Linear", {0: "LightGoldenRodYellow", 1: "#FFFF66"});
            var GatewayNodeSize = 80;
            var GatewayNodeSymbolSize = 45;
            var GatewayNodeFill = GradientYellow;
            var GatewayNodeStroke = "darkgoldenrod";
            var GatewayNodeSymbolStroke = "darkgoldenrod";
            var GatewayNodeSymbolFill = GradientYellow;
            var GatewayNodeSymbolStrokeWidth = 3;
            var EventNodeSize = 42;
            var EventNodeInnerSize = EventNodeSize - 6;
            var gatewayDrawProprty = {
                ParallelGateway: {"figure": "ThinCross"},
                ExclusiveGateway: {"figure": "ThinX"},
                EventGateway: {"figure": "Pentagon"}
            };
            function makeCircle(fSize) {
                var ret =
                        $(go.Shape, "Circle", {alignment: go.Spot.Center,
                            stroke: GatewayNodeSymbolStroke,
                            desiredSize: new go.Size(fSize, fSize),
                            fill: null},
                                new go.Binding("visible", "type", function (tpy) {
                                    return tpy === "EventGateway";
                                })
                                );
                return ret;
            }

            function makeFigure(isPalete) {
                var ret =
                        $(go.Shape, "NotAllowed",
                                {alignment: go.Spot.Center, stroke: GatewayNodeSymbolStroke, fill: GatewayNodeSymbolFill},
                                new go.Binding("figure", "type", function (tpy) {
                                    return gatewayDrawProprty[tpy].figure;
                                }),
                                new go.Binding("strokeWidth", "type",
                                        function (s) {
                                            return (s === "Event") ? 1 : GatewayNodeSymbolStrokeWidth;
                                        }),
                                new go.Binding("desiredSize", "type", function (tpy) {
                                    var size = new go.Size(GatewayNodeSymbolSize, GatewayNodeSymbolSize);
                                    if (tpy === "ExclusiveGateway") {
                                        size.width = size.width / 4 * 3;
                                        size.height = size.height / 4 * 3;
                                    } else if (tpy === "EventGateway") {
                                        size.width = size.width / 1.6;
                                        size.height = size.height / 1.6;
                                    }
                                    if (isPalete) {
                                        size.width = size.width / 2;
                                        size.height = size.height / 2;
                                    }
                                    return size;
                                }));
                return ret;
            }

            function makeText(editable) {
                var ret = $(go.TextBlock,
                        {alignment: go.Spot.Center, textAlign: "center", margin: 5, editable: editable},
                        new go.Binding("text").makeTwoWay());
                return ret;
            }

            this.getDiagramTemplate = function () {
                if (!this.diagramTemplate) {
                    this.diagramTemplate =
                            $(go.Node, "Vertical",
                                    {locationObjectName: "SHAPE",
                                        locationSpot: go.Spot.Center
                                                // toolTip: tooltiptemplate
                                    },
                                    new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
                                    new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify),
                                    {resizable: false, resizeObjectName: "SHAPE"},
                                    $(go.Panel, "Spot",
                                            $(go.Shape, "Diamond",
                                                    {strokeWidth: 1,
                                                        fill: GatewayNodeFill,
                                                        stroke: GatewayNodeStroke,
                                                        name: "SHAPE",
                                                        desiredSize: new go.Size(GatewayNodeSize, GatewayNodeSize),
                                                        portId: "", fromLinkable: true, toLinkable: true, cursor: "pointer"
                                                    },
                                                    new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify)
                                                    ), // end main shape
                                            makeFigure(),
                                            makeCircle(EventNodeSize),
                                            makeCircle(EventNodeInnerSize)
                                            ),
                                    makeText(true)
                                    ); // end go.Node Vertical      
                }
                return this.diagramTemplate;
            };
            this.getPaletteTemplate = function () {
                if (!this.paletteTemplate) {
                    this.paletteTemplate =
                            $(go.Node, "Vertical",
                                    {//toolTip: tooltiptemplate,
                                        resizable: false,
                                        locationObjectName: "SHAPE",
                                        locationSpot: go.Spot.Center,
                                        resizeObjectName: "SHAPE"},
                                    $(go.Panel, "Spot",
                                            $(go.Shape, "Diamond",
                                                    {strokeWidth: 1,
                                                        fill: GatewayNodeFill,
                                                        stroke: GatewayNodeStroke, name: "SHAPE",
                                                        desiredSize: new go.Size(GatewayNodeSize / 2, GatewayNodeSize / 2)
                                                    }
                                            ),
                                            makeFigure(true),
                                            makeCircle(EventNodeSize / 2),
                                            makeCircle(EventNodeInnerSize / 2)
                                            ),
                                    makeText(false)
                                    );
                }
                return this.paletteTemplate;
            };
        };

//----------------- EventTemplate -----------------------------------------------
        XS.graphObject.EventTemplate = function () {
            var $ = XS.graphObject.$;
            var EventNodeSize = 42;
            var EventNodeInnerSize = EventNodeSize - 6;
            var EventNodeSymbolSize = EventNodeInnerSize - 14;



            var eventDrawProprty = {
                "StartEvent": {"fill": "lightgreen",
                    "strokeWidth": 1,
                    "stroke": "green",
                    "fromLinkable": true
                },
                "TimerEvent": {
                    "fill": "lightgreen",
                    "strokeWidth": 1,
                    "stroke": "green",
                    "innerCircle": true,
                    "fromLinkable": true,
                    "toLinkable": true
                },
                "CatchMessageEvent": {
                    "fill": "lightgreen",
                    "strokeWidth": 1,
                    "stroke": "green",
                    "innerCircle": true,
                    "fromLinkable": true,
                    "toLinkable": true
                },
                "ThrowMessageEvent": {
                    "fill": "lightgreen",
                    "strokeWidth": 1,
                    "stroke": "green",
                    "innerCircle": true,
                    "fromLinkable": true,
                    "toLinkable": true
                },
                "EndEvent": {"fill": "pink",
                    "strokeWidth": 4,
                    "stroke": "red",
                    "toLinkable": true
                },
                "TerminateEvent": {"fill": "pink",
                    "strokeWidth": 4,
                    "stroke": "red",
                    "toLinkable": true
                }

            };
            var eventFigure = {
                "TerminateEvent": "Circle",
                "TimerEvent": "BpmnEventTimer",
                "CatchMessageEvent": "BpmnTaskMessage",
                "ThrowMessageEvent": "BpmnTaskMessage"
            };
            var eventFigureFill = {
                "TerminateEvent": "dimgray",
                "TimerEvent": "white",
                "CatchMessageEvent": "white",
                "ThrowMessageEvent": "dimgray"
            };

            function makeText(editable) {
                var ret = $(go.TextBlock,
                        {alignment: go.Spot.Center, textAlign: "center", margin: 5, editable: editable},
                        new go.Binding("text").makeTwoWay());
                return ret;
            }

            function makeOuterCircle() {
                var ret =
                        $(go.Shape, "Circle", // Outer circle
                                {strokeWidth: 1,
                                    name: "SHAPE",
                                    desiredSize: new go.Size(EventNodeSize, EventNodeSize),
                                    portId: "", fromLinkable: true, toLinkable: true, cursor: "pointer"
                                },
                        // allows the color to be determined by the node data
                                new go.Binding("fill", "type", function (tpy) {
                                    return eventDrawProprty[tpy].fill;
                                }),
                                new go.Binding("strokeWidth", "type", function (tpy) {
                                    return eventDrawProprty[tpy].strokeWidth;
                                }),
                                new go.Binding("stroke", "type", function (tpy) {
                                    return eventDrawProprty[tpy].stroke;
                                }),
                                new go.Binding("strokeDashArray", "type", function (tpy) {
                                    if (tpy === "TimerEvent")
                                        return [4, 2];
                                    return null;
                                }),
                                new go.Binding("desiredSize", "size", go.Size.parse).makeTwoWay(go.Size.stringify)
                                );
                return ret;
            }

            function makeInnerCircle() {
                var ret =
                        $(go.Shape, "Circle", // Inner circle
                                {alignment: go.Spot.Center, desiredSize: new go.Size(EventNodeInnerSize, EventNodeInnerSize), fill: null},
                                new go.Binding("strokeDashArray", "type", function (tpy) {
                                    if (tpy === "TimerEvent")
                                        return [4, 2];
                                    return null;
                                }), // dashes for non-interrupting
                                new go.Binding("visible", "type", function (tpy) {
                                    return eventDrawProprty[tpy].innerCircle;
                                })
                                );
                return ret;
            }

            function makeFigure() {
                var ret =
                        $(go.Shape, "NotAllowed",
                                {alignment: go.Spot.Center,
                                    desiredSize: new go.Size(EventNodeSymbolSize, EventNodeSymbolSize),
                                    stroke: "black"
                                },
                                new go.Binding("visible", "type", function (tpy) {
                                    return tpy === "TerminateEvent"
                                            || tpy === "TimerEvent"
                                            || tpy === "CatchMessageEvent"
                                            || tpy === "ThrowMessageEvent";
                                }),
                                new go.Binding("figure", "type", function (tpy) {
                                    return eventFigure[tpy];
                                }),
                                new go.Binding("fill", "type", function (tpy) {
                                    return eventFigureFill[tpy];
                                })

                                );
                return ret;
            }

            this.getPaletteTemplate = this.getDiagramTemplate = function () {
                if (!this.diagramTemplate) {
                    this.diagramTemplate = $(go.Node, "Vertical",
                            {locationObjectName: "SHAPE",
                                locationSpot: go.Spot.Center
                                        //toolTip: tooltiptemplate
                            },
                            new go.Binding("key", "key").makeTwoWay(),
                            new go.Binding("location", "loc", go.Point.parse).makeTwoWay(go.Point.stringify),
                            {resizable: false, resizeObjectName: "SHAPE"},
                            $(go.Panel, "Spot",
                                    makeOuterCircle(),
                                    makeInnerCircle(),
                                    makeFigure()
                                    ), // end Auto Panel
                            makeText(true)
                            );
                }
                return this.diagramTemplate;
            };
        };


        XS.graphObject.MessageFlowLinkTemplate = function () {
            var $ = XS.graphObject.$;

            this.getDiagramTemplate = function () {
                if (!this.diagramTemplate) {
                    this.diagramTemplate =
                            $(XS.graphObject.PoolLink, // defined in BPMNClasses.js
                                    new go.Binding("key", "key").makeTwoWay(),
                                    {routing: go.Link.Orthogonal, curve: go.Link.JumpGap, corner: 10,
                                        fromSpot: go.Spot.TopBottomSides, toSpot: go.Spot.TopBottomSides,
                                        reshapable: true, relinkableTo: true, toEndSegmentLength: 20},
                                    new go.Binding("points").makeTwoWay(),
                                    $(go.Shape, {stroke: "black", strokeWidth: 1, strokeDashArray: [6, 2]}),
                                    $(go.Shape, {toArrow: "Triangle", scale: 1, fill: "white", stroke: "black"}),
                                    $(go.Shape, {fromArrow: "Circle", scale: 1, visible: true, stroke: "black", fill: "white"}),
                                    $(go.TextBlock, {
                                        editable: true, text: "label"
                                    }, // Link label
                                            new go.Binding("text", "text").makeTwoWay())
                                    );
                }
                return this.diagramTemplate;
            };

            this.getPaletteTemplate = function () {
                if (!this.paletteTemplate) {
                    this.paletteTemplate =
                            $(XS.graphObject.PoolLink, // defined in BPMNClasses.js
                                    {locationSpot: go.Spot.Center,
                                        routing: go.Link.Orthogonal, curve: go.Link.JumpGap, corner: 10,
                                        fromSpot: go.Spot.TopBottomSides, toSpot: go.Spot.TopBottomSides,
                                        reshapable: true, relinkableTo: true, toEndSegmentLength: 20},
                                    new go.Binding("points").makeTwoWay(),
                                    $(go.Shape, {stroke: "black", strokeWidth: 1, strokeDashArray: [6, 2]}),
                                    $(go.Shape, {toArrow: "Triangle", scale: 1, fill: "white", stroke: "black"}),
                                    $(go.Shape, {fromArrow: "Circle", scale: 1, visible: true, stroke: "black", fill: "white"}),
                                    $(go.TextBlock, {
                                        editable: true,
                                    }, // Link label
                                            new go.Binding("text", "text").makeTwoWay())
                                    );

                }
                ;
                return this.paletteTemplate;
            };

        };


//------------------------SequenceFlowTemplate----------------------------------

        XS.graphObject.SequenceFlowTemplate = function () {
            var $ = XS.graphObject.$;

            this.getDiagramTemplate = function () {
                if (!this.diagramTemplate) {
                    this.diagramTemplate =
                            $(go.Link,
                                    {
                                        routing: go.Link.AvoidsNodes, curve: go.Link.JumpGap, corner: 10,
                                        //fromSpot: go.Spot.RightSide, toSpot: go.Spot.LeftSide,
                                        reshapable: true, relinkableFrom: true, relinkableTo: true, toEndSegmentLength: 20
                                    },
                                    new go.Binding("points").makeTwoWay(),
                                    $(go.Shape, {stroke: "black", strokeWidth: 1}),
                                    $(go.Shape, {toArrow: "Triangle", scale: 1.2, fill: "black", stroke: null}),
                                    $(go.Shape, {fromArrow: "", scale: 1.5, stroke: "black", fill: "white"},
                                            new go.Binding("fromArrow", "isDefault", function (s) {
                                                if (s === null)
                                                    return "";
                                                return s ? "BackSlash" : "StretchedDiamond";
                                            }),
                                            new go.Binding("segmentOffset", "isDefault", function (s) {
                                                return s ? new go.Point(5, 0) : new go.Point(0, 0);
                                            })),
                                    $(go.TextBlock, {// this is a Link label
                                        name: "Label", editable: true, text: "label", segmentOffset: new go.Point(-10, -10), visible: false
                                    },
                                            new go.Binding("text", "text").makeTwoWay(),
                                            new go.Binding("visible", "visible").makeTwoWay())
                                    );
                }
                ;
                return this.diagramTemplate;
            };
        };




// --------------- BpmnDiagram--------------------------------------------------
        XS.graphObject.BpmnDiagram = function (opt) {
            var self = this;
            var $ = XS.graphObject.$;
            var GRP = XS.graphObject.bpmngroup;
            this.selectionCb = opt.selectionCb;
            this.portselectionCb = opt.portselectionCb;
            this.diagramDiv = opt.diagramDiv;
            var taskTemplate = new XS.graphObject.TaskTemplate();
            var gatewayTemplate = new XS.graphObject.GatewayTemplate();
            var eventTemplate = new XS.graphObject.EventTemplate();
            var nodeTemplateMap = new go.Map("string", go.Node);
            nodeTemplateMap.add(GRP.task, taskTemplate.getDiagramTemplate());
            nodeTemplateMap.add(GRP.gateway, gatewayTemplate.getDiagramTemplate());
            nodeTemplateMap.add(GRP.event, eventTemplate.getDiagramTemplate());
            var sequenceFlowTemplate = new XS.graphObject.SequenceFlowTemplate();
            var messageFlowLinkTemplate = new XS.graphObject.MessageFlowLinkTemplate();
            var linkTemplateMap = new go.Map("string", go.Link);
            linkTemplateMap.add(GRP.sequence, sequenceFlowTemplate.getDiagramTemplate());
            linkTemplateMap.add(GRP.msg, messageFlowLinkTemplate.getDiagramTemplate());
            var poolTemplate = new XS.graphObject.PoolGroupTemplate();
            var swimLanesTemplate = new XS.graphObject.SwimLanesGroupTemplate();
            var groupTemplateMap = new go.Map("string", go.Group);
            groupTemplateMap.add(GRP.pool, poolTemplate.getDiagramTemplate());
            groupTemplateMap.add(GRP.lane, swimLanesTemplate.getDiagramTemplate());
            this.render = function () {

                if (!self.diagram) {
                    self.diagram =
                            $(go.Diagram, this.diagramDiv, {
                                linkingTool: new XS.graphObject.BPMNLinkingTool(),
                                "linkingTool.isUnconnectedLinkValid": true,
                                "draggingTool.dragsLink": true,
                                "linkingTool.portGravity": 20,
                                allowDrop: true,
                                initialContentAlignment: go.Spot.Center,
                                "undoManager.isEnabled": true,
                                "relinkingTool.fromHandleArchetype":
                                        $(go.Shape, "Diamond", {segmentIndex: 0, cursor: "pointer", desiredSize: new go.Size(8, 8), fill: "tomato", stroke: "darkred"}),
                                "relinkingTool.toHandleArchetype":
                                        $(go.Shape, "Diamond", {segmentIndex: -1, cursor: "pointer", desiredSize: new go.Size(8, 8), fill: "darkred", stroke: "tomato"}),
                                "SelectionMoved": function () {
                                    XS.graphObject.relayoutDiagram(self.diagram);
                                },
                                "SelectionCopied": function () {
                                    XS.graphObject.relayoutDiagram(self.diagram);
                                }
                            });
                    self.diagram.toolManager.mouseDownTools.insertAt(0, new XS.graphObject.LaneResizingTool());
                    self.diagram.nodeTemplateMap = nodeTemplateMap;
                    self.diagram.linkTemplateMap = linkTemplateMap;
                    self.diagram.groupTemplateMap = groupTemplateMap;
                    self.diagram.model.linkKeyProperty = "key";
                    self.diagram.addDiagramListener('ChangedSelection', self.onChangedSelection);
                    var iiii = 1;

                    self.diagram.model.makeUniqueLinkKeyFunction = function (model, obj) {
                        var ret = "_" + iiii;
                        iiii = iiii + 1;
                        return ret;
                    };

                }
            };

            this.checkUniqueKey = function (model, key) {

                if (model.modelData.default) {
                    if (model.modelData.default.process === key) {
                        return false;
                    }
                }

                if (model.modelData.messages) {
                    var messages = model.modelData.messages;
                    for (var mes in messages) {
                        if (messages.hasOwnProperty(mes)) {
                            if (mes === key) {
                                return false;
                            }
                            var messagePath = messages[mes].messagePath;
                            if (messagePath) {
                                for (var path in messagePath) {
                                    if (messagePath.hasOwnProperty(path)) {
                                        if (path === key) {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                if (model.modelData.interfaces) {
                    var ret = model.modelData.interfaces.some(function (intr) {
                        if (intr.id === key) {
                            return true;
                        }
                        var operations = intr.operations;
                        if (!operations) {
                            return false;
                        }
                        var opret = operations.some(function (op) {
                            if (op.id === key) {
                                return true;
                            }
                            return false;
                        });
                        return opret;
                    });
                    if (ret) {
                        return false;
                    }
                }

                if (model.findNodeDataForKey(key)) {
                    return false;
                }
                if (model.findLinkDataForKey(key)) {
                    return false;
                }
                var ok = model.nodeDataArray.find(function (node) {
                    var ret = false;
                    if (node.boundaryEventArray) {
                        ret = node.boundaryEventArray.find(function (port) {
                            if (port.portId === key) {
                                return true;
                            }
                            return false;
                        });
                    }
                    return ret;
                });
                if (ok) {
                    return false;
                }
                return true;
            };

            this.makeUniqueKey = function (model) {
                var i = 1;
                var prfx = "K_";

                var ret = prfx + i;
                while (!self.checkUniqueKey(model, ret)) {
                    i = i + 1;
                    ret = prfx + i;
                }
                return ret;
            };

            this.addChangedListener = function (evt) {
                if (!evt.isTransactionFinished)
                    return;
                var txn = evt.object; // a Transaction
                if (txn === null)
                    return;
                // iterate over all of the actual ChangedEvents of the Transaction
                txn.changes.each(function (e) {
                    // ignore any kind of change other than adding/removing a node
                    if (e.modelChange !== "nodeDataArray")
                        return;
                    // console.log(evt.model.modelData);
                    // record node insertions and removals
                    if (e.change === go.ChangedEvent.Insert) {
                        // console.log(evt.propertyName + " added node with key: " + e.newValue.key);
                        var newValue = e.newValue;
                        if (newValue.type === "Process") {
                            var modelData = evt.model.modelData;
                            var participants = modelData.participants || (modelData.participants = []);
                            participants[participants.length] = newValue.key;
                            //  console.log("new Process");
                            //  console.log(evt.model.modelData);
                        } else {
                            if (!newValue.group) {
                                if (newValue.category === XS.graphObject.bpmngroup.task
                                        || newValue.category === XS.graphObject.bpmngroup.gateway
                                        || newValue.category === XS.graphObject.bpmngroup.event
                                        ) {
                                    var modelData = evt.model.modelData;
                                    if (!modelData.default) {
                                        modelData.default = {
                                            process: self.makeUniqueKey(evt.model)
                                        };
                                    }
                                }


                            }
                        }

                        // console.log(newValue);
                    } else if (e.change === go.ChangedEvent.Remove) {
                        var oldValue = e.oldValue;
                        if (oldValue.type === "Process") {
                            var modelData = evt.model.modelData;
                            var participants = modelData.participants || (modelData.participants = []);
                            var idx = participants.indexOf(oldValue.key);
                            if (idx >= 0) {
                                participants.splice(idx, 1);
                            }
                            modelData.participants = participants;
                        } else {
                            if (!oldValue.group) {
                                if (oldValue.category === XS.graphObject.bpmngroup.task
                                        || oldValue.category === XS.graphObject.bpmngroup.gateway
                                        || oldValue.category === XS.graphObject.bpmngroup.event
                                        ) {
                                    var model = evt.model;
                                    var testobj = model.nodeDataArray.find(function (obj) {
                                        if (!obj.group) {
                                            return true;
                                        }
                                        return false;
                                    });
                                    if (!testobj) {
                                        delete model.modelData.default;
                                        self.diagram.ISDIAGRAM = true;
                                        self.selectionCb(self.diagram);

                                    }
                                    //    console.log(model.nodeDataArray);
                                }
                            }

                            //console.log(evt.propertyName + " removed node with key: " + oldValue.key);
                            //console.log(oldValue);
                        }
                    }
                });
            };

            this.getInterfaces = function () {
                var ret = self.diagram.model.modelData.interfaces || (self.diagram.model.modelData.interfaces = []);
                return ret;
            };

            this.createInterface = function (name) {
                var interfaces = self.getInterfaces();
                var model = self.diagram.model;
                var key = self.makeUniqueKey(model);
                var ret = {id: key, name: name, implementationRef: 'mbean', operations: []};
                interfaces.push(ret);
                return ret;
            };
            this.createOperation = function (interId, name) {
                var interfaces = self.getInterfaces();
                var inter = interfaces.find(function (elem) {
                    if (elem.id === interId) {
                        return true;
                    }
                    return false;
                });
                var operations = inter.operations || (inter.operations = []);
                var model = self.diagram.model;
                var key = self.makeUniqueKey(model);
                var ret = {id: key, name: name};
                operations.push(ret);
                return ret;
            };
            this.changeInterfaceKey = function (oldKey, newKey) {
                var interfaces = self.getInterfaces();
                interfaces.forEach(function (inter) {
                    if (inter.id === oldKey) {
                        inter.id = newKey;
                    }
                    inter.operations.forEach(function (op) {
                        if (op.id === oldKey) {
                            op.id = newKey;
                        }
                    });
                });
            };
            this.destroyInterface = function (key) {
                var interfaces = self.getInterfaces();
                var idx = interfaces.findIndex(function (elem) {
                    if (elem.id === key) {
                        return true;
                    }
                    return false;
                });
                if (idx >= 0) {
                    interfaces.splice(idx, 1);
                }
                self.diagram.model.modelData.interfaces = interfaces;
            };

            this.destroyOperation = function (key) {
                var interfaces = self.getInterfaces();
                var inter = interfaces.find(function (interelem) {
                    if (!interelem.operations) {
                        return false;
                    }
                    var idx = interelem.operations.findIndex(function (opelem) {
                        if (opelem.id === key) {
                            return true;
                        }
                        return false;
                    });
                    if (idx >= 0) {
                        return true;
                    }
                    return false;
                });
                if (inter) {
                    var operations = inter.operations || (inter.operations = []);
                    var idx = operations.findIndex(function (elem) {
                        if (elem.id === key) {
                            return true;
                        }
                        return false;
                    });
                    if (idx >= 0) {
                        operations.splice(idx, 1);
                        inter.operations = operations;
                    }
                }
            };



            this.createBpmMessage = function (key) {
                var messages = self.diagram.model.modelData.messages || (self.diagram.model.modelData.messages = {});
                messages[key] = {};
                self.diagram.model.modelData.messages = messages;
            };

            this.changeMessageKey = function (oldvalue, newvalue) {
                var message = self.diagram.model.modelData.messages[oldvalue];
                var model = self.diagram.model;
                var nodeDataArray = model.nodeDataArray;

                var newmessage = {messagePath: message.messagePath};
                delete self.diagram.model.modelData.messages[oldvalue];
                self.diagram.model.modelData.messages[newvalue] = newmessage;

                nodeDataArray.forEach(function (node) {
                    if (node.message) {
                        if (node.message === oldvalue) {
                            node.message = newvalue;
                        }
                    }
                    if (node.subscriptions) {
                        var subscr = node.subscriptions;
                        if (subscr.hasOwnProperty(oldvalue)) {
                            var subscrItem = subscr[oldvalue];
                            delete subscr[oldvalue];
                            subscr[newvalue] = subscrItem;
                        }
                    }
                });
                var model = self.diagram.model;


                var linkDataArray = model.linkDataArray;
                linkDataArray.forEach(function (node) {
                    if (node.message) {
                        if (node.message === oldvalue) {
                            node.message = newvalue;
                        }
                    }
                }
                );

            };

            this.createBpmMessagePath = function (msg, pathkey) {
                var message = self.diagram.model.modelData.messages[msg];
                var messagePath = message.messagePath || (message.messagePath = {});
                messagePath[pathkey] = "";
            };

            this.changeMessagePathKey = function (mes, oldvalue, newvalue) {
                var message = self.diagram.model.modelData.messages[mes];
                var oldValuePath = message.messagePath[oldvalue];
                delete message.messagePath[oldvalue];
                message.messagePath[newvalue] = oldValuePath;
                var model = self.diagram.model;
                var nodeDataArray = model.nodeDataArray;
                nodeDataArray.forEach(function (node) {
                    if (node.subscriptions) {
                        var subscr = node.subscriptions;
                        if (subscr.hasOwnProperty(mes)) {
                            var keys = subscr[mes];
                            var newKeys = [];
                            keys.forEach(function (curkey) {
                                if (curkey.hasOwnProperty(oldvalue)) {
                                    var remamed = {};
                                    var save = curkey[oldvalue];
                                    remamed[newvalue] = save;
                                    newKeys.push(remamed);
                                } else {
                                    newKeys.push(curkey);
                                }
                            });
                            subscr[mes] = newKeys;
                        }
                    }
                });

            };

            this.setMessagePathValue = function (mes, pathKey, newValue) {
                var message = self.diagram.model.modelData.messages[mes];
                message.messagePath[pathKey] = newValue;
            };

            this.removeMessagePath = function (mes, pathKey) {
                var message = self.diagram.model.modelData.messages[mes];
                delete message.messagePath[pathKey];

                var model = self.diagram.model;
                var nodeDataArray = model.nodeDataArray;
                nodeDataArray.forEach(function (node) {
                    if (node.subscriptions) {
                        var subscr = node.subscriptions;
                        if (subscr.hasOwnProperty(mes)) {
                            var keys = subscr[mes];
                            var newKeys = [];
                            keys.forEach(function (curkey) {
                                if (!curkey.hasOwnProperty(pathKey)) {
                                    newKeys.push(curkey);
                                }
                            });
                            subscr[mes] = newKeys;
                        }
                    }
                });


            };



            this.removeMessage = function (mes) {
                var messages = self.diagram.model.modelData.messages;
                var model = self.diagram.model;
                var nodeDataArray = model.nodeDataArray;
                nodeDataArray.forEach(function (node) {
                    if (node.message) {
                        if (node.message === mes) {
                            node.message = "";
                        }
                    }

                    if (node.subscriptions) {
                        var subscr = node.subscriptions;
                        if (subscr.hasOwnProperty(mes)) {
                            delete subscr[mes];
                        }
                    }
                });

                var linkDataArray = model.linkDataArray;
                linkDataArray.forEach(function (node) {
                    if (node.message) {
                        if (node.message === mes) {
                            node.message = "";
                        }
                    }
                }
                );

                delete messages[mes];
            };

            this.getMessage = function (mes) {
                var messages = self.diagram.model.modelData.messages;
                return messages[mes];
            };

            this.getProsess = function (groupKey) {
                var ret = self.diagram.model.findNodeDataForKey(groupKey);
                while (ret.type !== "Process") {
                    var key = ret.group;
                    ret = self.diagram.model.findNodeDataForKey(key);
                }
                return ret;
            };
            this.setModel = function (jsondata) {
                self.diagram.model = go.Model.fromJson(jsondata);
                self.diagram.model.linkFromPortIdProperty = "fromPort";
                self.diagram.model.linkToPortIdProperty = "toPort";
                self.diagram.model.makeUniqueLinkKeyFunction = self.makeUniqueKey;
                self.diagram.model.makeUniqueKeyFunction = self.makeUniqueKey;
                self.diagram.model.checkUniqueKey = self.checkUniqueKey;
                self.diagram.model.createBpmMessage = self.createBpmMessage;
                self.diagram.model.createBpmMessagePath = self.createBpmMessagePath;
                self.diagram.model.changeMessageKey = self.changeMessageKey;
                self.diagram.model.changeMessagePathKey = self.changeMessagePathKey;
                self.diagram.model.setMessagePathValue = self.setMessagePathValue;
                self.diagram.model.removeMessagePath = self.removeMessagePath;
                self.diagram.model.removeMessage = self.removeMessage;
                self.diagram.model.getProsess = self.getProsess;
                self.diagram.model.getMessage = self.getMessage;
                self.diagram.model.portselectionCb = self.portselectionCb;
                self.diagram.model.getInterfaces = self.getInterfaces;
                self.diagram.model.createInterface = self.createInterface;
                self.diagram.model.changeInterfaceKey = self.changeInterfaceKey;
                self.diagram.model.createOperation = self.createOperation;
                self.diagram.model.destroyInterface = self.destroyInterface;

                self.diagram.model.destroyOperation= self.destroyOperation;

                self.diagram.model.addChangedListener(self.addChangedListener);

                self.diagram.model.undoManager.isEnabled = true;
                self.diagram.isModified = false;
            };
            this.getSelection = function () {
                var diagram = self.diagram;
                var selection = diagram.selection.first();
                if (!selection) {
                    diagram.ISDIAGRAM = true;
                    return diagram;
                }
                return selection;
            };
            this.onChangedSelection = function (e) {

                var diagram = e.diagram;
                if (!diagram) {
                    return;
                }
                var selection = diagram.selection.first();
                if (self.selectionCb) {
                    if (!selection) {
                        diagram.ISDIAGRAM = true;
                        self.selectionCb(diagram);
                    } else {
                        self.selectionCb(selection);
                    }
                }

            };
            this.onUpdateViews = function (e) {
                console.log(e); //alert(e);
            };
            this.doLayout = function () {
                //  this.diagram.layoutDiagram(true);
                XS.graphObject.relayoutDiagram(this.diagram);
            };
            this.toJson = function () {
                var ret = this.diagram.model.toJson();
                return ret;
            };
        };
// --------------- BpmnPalete---------------------------------------------------
        XS.graphObject.BpmnPalete = function (opt) {
            var BT = XS.graphObject.bpmntype;
            var GRP = XS.graphObject.bpmngroup;
            var $ = XS.graphObject.$;
            this.diagramDiv = opt.paleteDiv;
            var taskTemplate = new XS.graphObject.TaskTemplate();
            var gatewayTemplate = new XS.graphObject.GatewayTemplate();
            var eventTemplate = new XS.graphObject.EventTemplate();
            var nodeTemplateMap = new go.Map("string", go.Node);
            nodeTemplateMap.add(GRP.task, taskTemplate.getPaletteTemplate());
            nodeTemplateMap.add(GRP.gateway, gatewayTemplate.getPaletteTemplate());
            nodeTemplateMap.add(GRP.event, eventTemplate.getPaletteTemplate());
            // Start End Timer Terminate CatchMessage ThrowMessage

            //var sequenceFlowTemplate = new XS.graphObject.SequenceFlowTemplate();
            //var messageFlowLinkTemplate = new XS.graphObject.MessageFlowLinkTemplate();
            //var linkTemplateMap = new go.Map("string", go.Link);
            //linkTemplateMap.add(GRP.sequence, sequenceFlowTemplate.getPaletteTemplate());
            //linkTemplateMap.add(GRP.msg, messageFlowLinkTemplate.getPaletteTemplate());
            var poolTemplate = new XS.graphObject.PoolGroupTemplate();
            var swimLanesTemplate = new XS.graphObject.SwimLanesGroupTemplate();
            var groupTemplateMap = new go.Map("string", go.Group);
            groupTemplateMap.add(GRP.pool, poolTemplate.getPaletteTemplate());
            groupTemplateMap.add(GRP.lane, swimLanesTemplate.getPaletteTemplate());
            this.render = function () {
                if (!this.diagram) {


                    this.diagram = $(go.Palette, this.diagramDiv, {
                        maxSelectionCount: 1,
                        nodeTemplateMap: nodeTemplateMap,
                        //   linkTemplateMap: linkTemplateMap,
                        groupTemplateMap: groupTemplateMap,
                        model: new go.GraphLinksModel([
                            {key: "Pool", text: "Pool 1", "isGroup": "true", "category": GRP.pool, type: BT.Process},
                            {key: "Lane", "text": "Lane 1", "isGroup": "true", "group": "Pool", "category": GRP.lane, type: BT.Lane},
                            {key: "event", text: "Start", category: GRP.event, type: BT.StartEvent},
                            {key: "event", text: "End", category: GRP.event, type: BT.EndEvent},
                            {key: "event", text: "Terminate", category: GRP.event, type: BT.TerminateEvent},
                            {key: "event", text: "Timer", category: GRP.event, type: BT.TimerEvent},
                            {key: "event", text: "Catch\nMessage", category: GRP.event, type: BT.CatchMessageEvent},
                            {key: "event", text: "Throw\nMessage", category: GRP.event, type: BT.ThrowMessageEvent},
                            {key: "task", text: "User", category: GRP.task, type: BT.UserTask},
                            {key: "task", text: "Service", category: GRP.task, type: BT.ServiceTask},
                            {key: "task", text: "Script", category: GRP.task, type: BT.ScriptTask},
                            {key: "task", text: "Receive", category: GRP.task, type: BT.ReceiveTask},
                            {key: "task", text: "Send", category: GRP.task, type: BT.SendTask},
                            {key: "gateway", text: "Parallel", category: GRP.gateway, type: BT.ParallelGateway},
                            {key: "gateway", text: "Exclusive", category: GRP.gateway, type: BT.ExclusiveGateway},
                            {key: "gateway", text: "Event", category: GRP.gateway, type: BT.EventGateway}

                        ]//,
                                /*        [// new go.Point(30, 0), new go.Point(30, 40),
                                 {category: GRP.sequence, points: new go.List(go.Point).addAll(
                                 [
                                 new go.Point(0, 0), new go.Point(80, 0)
                                 ]),
                                 key: "seq",
                                 condition: "A>7",
                                 type: BT.SequenceFlow
                                 },
                                 {category: "msg", points: new go.List(go.Point).addAll(
                                 [
                                 new go.Point(0, 0), new go.Point(80, 0)
                                 ]),
                                 key: "msg",
                                 type: BT.MessageFlow
                                 }
                                 ] */
                                )

                    });
                }

            };
            this.doLayout = function () {
                this.diagram.layoutDiagram(true);
            };
        };



