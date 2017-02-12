/* 
 * Copyright (C) 2017 Andrey Timofeev
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

Ext.define('BPMN.view.BpmnPropertyGrid', {
    extend: 'Ext.Panel',
    requires: ['Ext.grid.property.Grid',
        'BPMN.view.WholeValueFld',
        'BPMN.view.AssignmentFld',
        'BPMN.view.MessageMngrFld',
        'BPMN.view.SubscriptionFld',
        'BPMN.view.InterfacesFld'
    ],
    alias: 'widget.BPMN-BpmnPropertyGrid',
    initComponent: function () {
        var self = this;

        self.saveCb = function (value) {
            // var ev=Ext.encode(value);
            self.grid.getSelectionModel().getSelection()[0].set('value', value);
        };

        self.saveCbEC = function (value) {
            var ev = Ext.encode(value);
            self.grid.getSelectionModel().getSelection()[0].set('value', ev);
        };

        self.sourceConfig = {
            type: {
                editor: Ext.create('Ext.form.DisplayField')
            },
            to: {
                editor: Ext.create('Ext.form.DisplayField')
            },
            from: {
                editor: Ext.create('Ext.form.DisplayField')
            },
            condition: {
                editor: Ext.create('BPMN.view.WholeValueFld', {saveCb: self.saveCb})

            },
            script:{
                editor: Ext.create('BPMN.view.WholeValueFld', {saveCb: self.saveCb,width_win:400,height_win:400})
            },
            time: {
                editor: Ext.create('BPMN.view.WholeValueFld', {saveCb: self.saveCb})
            },
            input: {
                editor: Ext.create('BPMN.view.AssignmentFld', {saveCb: self.saveCbEC, getModel: function () {
                        return self.inspectedObject;
                    }})
            },
            output: {
                editor: Ext.create('BPMN.view.AssignmentFld', {saveCb: self.saveCbEC, getModel: function () {
                        return self.inspectedObject;
                    }})
            },
            role: {
                editor: Ext.create('BPMN.view.WholeValueFld', {saveCb: self.saveCb})
            },
            user: {
                editor: Ext.create('BPMN.view.WholeValueFld', {saveCb: self.saveCb})
            },
            formKey: {
                editor: Ext.create('BPMN.view.WholeValueFld', {saveCb: self.saveCb})
            },
            formContext:{
                editor: Ext.create('BPMN.view.WholeValueFld', {saveCb: self.saveCb,width_win:400,height_win:400})
            },
            name: {
                editor: Ext.create('BPMN.view.WholeValueFld', {saveCb: self.saveCb})
            },
            message: {
                editor: Ext.create('BPMN.view.MessageMngrFld', {
                    saveCb: self.saveCb,
                    getModel: function () {
                        return self.inspectedObject;
                    }
                })
            },
            subscription: {
                editor: Ext.create('BPMN.view.SubscriptionFld', {
                    saveCb: self.saveCb,
                    getModel: function () {
                        return self.inspectedObject;
                    }
                })
            },
            operation:{
                editor: Ext.create('BPMN.view.InterfacesFld', {
                    saveCb: self.saveCb,
                    getModel: function () {
                        return self.inspectedObject;
                    }
                })
            }
        };


        var source = {};

        self.grid = Ext.create('Ext.grid.property.Grid', {
            sortableColumns: false,
            source: source,
            sourceConfig: self.sourceConfig,
            listeners: {
                propertychange: function (source, recordId, value, oldValue) {
                    self.changeProperty(recordId, value, oldValue);
                }
            }
        });

        self.items = [self.grid];
        //'propertychange'

        self.callParent(arguments);
    },
    getNewKey: function (diagram, newValue) {
        var model = diagram.model;
        var ret = newValue;
        if (!this.inspectedObject.ISDIAGRAM) {
            var defKey = this.inspectedObject.diagram.model.modelData.key;
            if (defKey === ret) {
                ret = ret + 1;
            }
        }
        var ret = newValue;
        while (model.findNodeDataForKey(ret)) {
            ret = ret + 1;
        }
        ;
        if (ret !== newValue) {
            this.grid.setProperty("id", ret);
        }
        ;

        return ret;
    },
    changeVid: function (diagram, modelData, newValue, oldValue) {
        var model = diagram.model;
        var key = this.getNewKey(diagram, newValue);
        if (!this.inspectedObject.ISDIAGRAM) {
            model.setKeyForNodeData(modelData, key);
            var type = modelData.type;
            if (type === "Process") {
                var participants = model.modelData.participants;
                var index = participants.indexOf(oldValue);
                participants[index] = key;
            }
        } else {
            model.setDataProperty(modelData, "key", key);
        }
    },
    changeVname: function (diagram, modelData, newValue) {
        var model = diagram.model;
        model.setDataProperty(modelData, "text", newValue);
    },
    changeVcondition: function (diagram, modelData, newValue) {
        var model = diagram.model;
        model.setDataProperty(modelData, "condition", newValue);
    },
    changeVscript: function (diagram, modelData, newValue) {
        var model = diagram.model;
        model.setDataProperty(modelData, "script", newValue);
    },
    
    changeVprocessName: function (diagram, modelData, newValue) {
        var model = diagram.model;
        var def = modelData.default;
        def.name = newValue;
        model.setDataProperty(modelData, "default", def);
    },
    changeVprocessId: function (diagram, modelData, newValue) {
        var model = diagram.model;
        var def = modelData.default;
        def.process = newValue;
        model.setDataProperty(modelData, "default", def);
    },
    changeVrole: function (diagram, modelData, newValue) {
        var model = diagram.model;
        model.setDataProperty(modelData, "role", newValue);
    },
    changeVuser: function (diagram, modelData, newValue) {
        var model = diagram.model;
        model.setDataProperty(modelData, "user", newValue);
    },
    changeVformKey: function (diagram, modelData, newValue) {
        var model = diagram.model;
        model.setDataProperty(modelData, "formKey", newValue);
    },
    changeVmessage: function (diagram, modelData, newValue) {
        var model = diagram.model;
        model.setDataProperty(modelData, "message", newValue);
    },
    changeVinput: function (diagram, modelData, newValue) {
        var model = diagram.model;
        var v = Ext.decode(newValue);
        model.setDataProperty(modelData, "input", v);
    },
    changeVoutput: function (diagram, modelData, newValue) {
        var model = diagram.model;
        var v = Ext.decode(newValue);
        model.setDataProperty(modelData, "output", v);
    },
    changeVtime: function (diagram, modelData, newValue) {
        var model = diagram.model;
        model.setDataProperty(modelData, "time", newValue);
    },
    
    changeVformContext: function (diagram, modelData, newValue) {
        var model = diagram.model;
        model.setDataProperty(modelData, "formContext", newValue);
    },
    
    changeVoperation:function (diagram, modelData, newValue) {
        var model = diagram.model;
        model.setDataProperty(modelData, "operation", newValue);
    },
    // findNodeDataForKey(key) 
    changeProperty: function (name, newValue, oldValue) {
        var inspectedObject = this.inspectedObject;
        var diagram = (inspectedObject.ISDIAGRAM) ? inspectedObject : inspectedObject.diagram;
        var modelData = (inspectedObject.ISDIAGRAM) ? inspectedObject.model.modelData : inspectedObject.data;
        diagram.startTransaction('set all properties');
        var fn = "changeV" + name;
        if (this[fn]) {
            this[fn](diagram, modelData, newValue, oldValue);
        }
        diagram.commitTransaction('set all properties');
    },
    setSimpleProp: function (src, name, value) {
        if (value) {
            src[name] = value;
        } else {
            src[name] = null;
        }
    },
    setBase: function (tgt, src) {
        var id = tgt.key;
        var name = tgt.text;
        this.setSimpleProp(src, "id", id);
        this.setSimpleProp(src, "name", name);
    },
    setDefinitions: function (source) {
        var modelData = this.inspectedObject.model.modelData;
        this.setBase(modelData, source);
        if (modelData.default) {
            this.setSimpleProp(source, "processId", modelData.default.process);
            this.setSimpleProp(source, "processName", modelData.default.name);
        }
        this.grid.setSource(source);
    },
    setStartEvent: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        var message = modelData.message;
        this.setSimpleProp(source, "message", message);
        var output = modelData.output;
        if (!output) {
            this.setSimpleProp(source, "output", "[]");
        } else {
            var ev = Ext.encode(output);
            this.setSimpleProp(source, "output", ev);
        }
        this.grid.setSource(source);
    },
    setSequenceFlow: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);

        var to = modelData.to;
        var from = modelData.from;
        var condition = modelData.condition;
        this.setSimpleProp(source, "to", to);
        this.setSimpleProp(source, "from", from);
        this.setSimpleProp(source, "condition", condition);

        this.grid.setSource(source);
    },
    setEndEvent: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        this.grid.setSource(source);
    },
    setTerminateEvent: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        this.grid.setSource(source);
    },
    setTimerEvent: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        var time = modelData.time;
        this.setSimpleProp(source, "time", time);
        this.grid.setSource(source);
    },
    setCatchMessageEvent: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        var message = modelData.message;
        this.setSimpleProp(source, "message", message);
        var output = modelData.output;
        if (!output) {
            this.setSimpleProp(source, "output", "[]");
        } else {
            var ev = Ext.encode(output);
            this.setSimpleProp(source, "output", ev);
        }
        this.setSimpleProp(source, 'subscription', 'subscription');
        this.grid.setSource(source);
    },
    setThrowMessageEvent: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        var message = modelData.message;
        this.setSimpleProp(source, "message", message);
        var input = modelData.input;
        if (!input) {
            this.setSimpleProp(source, "input", "[]");
        } else {
            var ev = Ext.encode(input);
            this.setSimpleProp(source, "input", ev);
        }

        this.grid.setSource(source);
    },
    setUserTask: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        var formKey = modelData.formKey;
        this.setSimpleProp(source, "formKey", formKey);
        var formContext = modelData.formContext;
        this.setSimpleProp(source,"formContext",formContext);
        var user = modelData.user;
        this.setSimpleProp(source, "user", user);
        var role = modelData.role;
        this.setSimpleProp(source, "role", role);
        var output = modelData.output;
        if (!output) {
            this.setSimpleProp(source, "output", "[]");
        } else {
            var ev = Ext.encode(output);
            this.setSimpleProp(source, "output", ev);
        }
        var input = modelData.input;
        if (!input) {
            this.setSimpleProp(source, "input", "[]");
        } else {
            var ev = Ext.encode(input);
            this.setSimpleProp(source, "input", ev);
        }
        this.grid.setSource(source);
    },
    setServiceTask: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        var operation = modelData.operation;
        this.setSimpleProp(source, "operation", operation);
        var output = modelData.output;
        if (!output) {
            this.setSimpleProp(source, "output", "[]");
        } else {
            var ev = Ext.encode(output);
            this.setSimpleProp(source, "output", ev);
        }
        var input = modelData.input;
        if (!input) {
            this.setSimpleProp(source, "input", "[]");
        } else {
            var ev = Ext.encode(input);
            this.setSimpleProp(source, "input", ev);
        }
        this.grid.setSource(source);
    },
    setScriptTask: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        var script= modelData.script;
        this.setSimpleProp(source, "script",script);
        var output = modelData.output;
        if (!output) {
            this.setSimpleProp(source, "output", "[]");
        } else {
            var ev = Ext.encode(output);
            this.setSimpleProp(source, "output", ev);
        }
        var input = modelData.input;
        if (!input) {
            this.setSimpleProp(source, "input", "[]");
        } else {
            var ev = Ext.encode(input);
            this.setSimpleProp(source, "input", ev);
        }
        
        this.grid.setSource(source);
        
    },
    setReceiveTask: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        var message = modelData.message;
        this.setSimpleProp(source, "message", message);
        var output = modelData.output;
        if (!output) {
            this.setSimpleProp(source, "output", "[]");
        } else {
            var ev = Ext.encode(output);
            this.setSimpleProp(source, "output", ev);
        }
        this.setSimpleProp(source, 'subscription', 'subscription');
        this.grid.setSource(source);
    },
    setSendTask: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        var message = modelData.message;
        this.setSimpleProp(source, "message", message);
        var input = modelData.input;
        if (!input) {
            this.setSimpleProp(source, "input", "[]");
        } else {
            var ev = Ext.encode(input);
            this.setSimpleProp(source, "input", ev);
        }
        this.grid.setSource(source);

    },
    setParallelGateway: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        this.grid.setSource(source);
    },
    setExclusiveGateway: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        this.grid.setSource(source);
    },
    setEventGateway: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        this.grid.setSource(source);
    },
    setProcess: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        this.grid.setSource(source);
    },
    setLane: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        this.grid.setSource(source);
    },
    setMessageFlow: function (source) {
        var modelData = this.inspectedObject.data;
        this.setBase(modelData, source);
        var message = modelData.message;
        this.setSimpleProp(source, "message", message);
        var from = modelData.from;
        this.setSimpleProp(source, "from", from);
        var to = modelData.to;
        this.setSimpleProp(source, "to", to);
        this.grid.setSource(source);
    },
    setData: function (inspectedObject) {
        this.inspectedObject = inspectedObject;

        var source = {};
        if (inspectedObject.ISDIAGRAM) {
            source.type = "Definitions";
        } else {
            //   console.log(inspectedObject.data);
            source.type = inspectedObject.data.type;
        }
        ;
        // console.log(source.type);
        var fn = "set" + source.type;
        if (this[fn]) {
            this[fn](source)
        } else {
            this.grid.setSource(source);
        }
    }
});
