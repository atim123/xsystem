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



Ext.define('BPMN.controller.BPMNEditor', {
    extend: 'Ext.app.Controller',
    views: ['BPMN.view.MainPanel',
        'BPMN.view.BpmnEditorDiagram',
        'BPMN.view.BpmnPropertyGrid',
        'BPMN.view.MessageMngr',
        'BPMN.view.PortProperyWindow',
        'BPMN.view.DefListDlg',
        'BPMN.view.MbeansOpDlg'
    ],
    refs: [
        {ref: 'diagram', selector: 'BPMN-BpmnEditorDiagram'},
        {ref: 'propertyGrid', selector: 'BPMN-BpmnPropertyGrid'}
    ],
    init: function () {
        this.control({
            'BPMN-mainpanel menuitem[action=savedefenition]': {
                click: this.onSaveDefenition
            },
            'BPMN-mainpanel menuitem[action=loaddefenition]': {
                click: this.startLoadDefinition
            },
            'BPMN-mainpanel menuitem[action=newdiagram]': {
                click: this.newDiagram
            },
            'BPMN-mainpanel button[action=testaction]': {
                click: this.onTestDlg
                        //onTestDlg
            }
        });
        this.application.on({
            DiagramChangedSelection: {
                fn: this.onDiagramChangedSelection,
                scope: this
            },
            PortDiagramSelection:{
                fn: this.onPortDiagramSelection,
                scope: this
            },
            LoadTemplate:{
                fn: this.loadDefinition,
                scope: this
            }
        });
    },
    onPortDiagramSelection:function(data){
         
       var win = Ext.create('BPMN.view.PortProperyWindow', {width: 900
            , height: 400});
      //  tt.setValue([{from: "A", to: "CC"}]);
        win.show();
        win.setData(data);
    },
    onDiagramChangedSelection: function (data) {
        var propertyGrid = this.getPropertyGrid();
        propertyGrid.setData(data);
    },
    ajaxFailure: function (response, opts) {
        var text = response.responseText;
        Ext.Msg.alert('Failure', text);
    },
    onSaveDefenition: function () {
        var diagram = this.getDiagram();
        var ret = diagram.toJson();
        var self = this;
        var url = "rest/bpmneditor/save";
        Ext.Ajax.request({
            url: url,
            jsonData: ret,
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('OK', text);
            },
            failure: self.ajaxFailure
        });
        //alert(ret);  
    },
    startLoadDefinition: function () {
      var dlg= Ext.create('BPMN.view.DefListDlg',{});
      dlg.show();
      /*  var self = this;
        Ext.Msg.prompt('Шаблон', 'Имя шаблона:', function (btn, text) {
            if (btn === 'ok') {
                self.loadDefinition(text);
            }
        });
      */  
    },
    loadDefinition: function (key) {
        var self = this;
        var url = "rest/bpmneditor/get/" + key;
        Ext.Ajax.request({
            url: url,
            jsonData: {},
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;
                var json = self.getJsonObject(text); //data
                var model = json.data;
                var diagram = self.getDiagram();
                diagram.setModel(model);
            },
            failure: self.ajaxFailure

        });
    },
    newDiagram: function () {
        var self = this;
        var diagram = self.getDiagram();
        diagram.newDiagram();
    },
    
    
    
    onTestDlg: function () {
        var tt = Ext.create('BPMN.view.MbeansOpDlg', {width: 900
            , height: 400});
      //  tt.setValue([{from: "A", to: "CC"}]);
        tt.show();
      //var json=  this.getTTT();
      //var diagram = this.getDiagram();
     //           diagram.setModel(json);
    },
    
    
    onTestAjax:function(){
        var self = this;
        var url = "rest/bpmneditor/test";
        Ext.Ajax.request({
            url: url,
            jsonData: {},
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('OK', text);
               /*
                var json = self.getJsonObject(text); //data
                var model = json.data;
                var diagram = self.getDiagram();
                diagram.setModel(model);
               */ 
                /*
                var text = response.responseText;
                Ext.Msg.alert('OK', text);
                */
            },
            failure: self.ajaxFailure

        });
    },
    
    getJsonObject: function (valueTxt) {
        if (!valueTxt) {
            return {};
        }

        valueTxt = valueTxt.trim();
        if (valueTxt === "") {
            return {};
        }
        try {
            if (valueTxt) {
                var data = eval('(' + valueTxt + ')');
                if (!Ext.isObject(data)) {
                    alert("Ошибка разора текста");
                    return null;
                }
                return data;
            }
        } catch (e) {
            if (e instanceof SyntaxError) {
                alert(e.message);
            } else {
                alert(e);
            }
            return null;
        }
    },
    
    getTTT:function(){
        var ret=
            {
    "class": "go.GraphLinksModel",
    "linkKeyProperty": "key",
    "linkFromPortIdProperty": "fromPort",
    "linkToPortIdProperty": "toPort",
    "modelData": {
        "key": "T67",
        "text": "",
        "participants": ["request_single_consultation", "response_single_consultation"],
        "messages": {}
    },
    "nodeDataArray": [{
            "key": "request_single_consultation",
            "text": "Запрос на разовую консультацию",
            "type": "Process",
            "category": "pool",
            "isGroup": "true",
            "loc": "-445.2064311523438 -399.87003740234377",
            "subscriptions": {}
        }, {
            "key": "Lane",
            "group": "request_single_consultation",
            "text": "Lane 1",
            "type": "Lane",
            "category": "lane",
            "isGroup": "true",
            "loc": "-445.2064311523438 -399.87003740234377",
            "size": "761 130.10000000000002"
        }, {
            "key": "event",
            "text": "Start",
            "type": "StartEvent",
            "category": "event",
            "group": "Lane",
            "message": "show_risk",
            "output": [],
            "loc": "-393.2064311523437 -354.87003740234377"
        }, {
            "key": "task",
            "text": "Просмотр ЛК обратившегося пользователя",
            "type": "UserTask",
            "category": "task",
            "group": "Lane",
            "input": [],
            "output": [],
            "loc": "-270.20643115234384 -347.87003740234377"
        }, {
            "key": "event6",
            "text": "Throw\nMessage",
            "type": "ThrowMessageEvent",
            "category": "event",
            "group": "Lane",
            "input": [],
            "loc": "-119.20643115234378 -341.87003740234377"
        }, {
            "key": "response_single_consultation",
            "text": "Разовая консультация",
            "type": "Process",
            "category": "pool",
            "isGroup": "true",
            "loc": "-435.9133936523437 -205.05991240234377",
            "subscriptions": {}
        }, {
            "key": "K_3",
            "group": "response_single_consultation",
            "text": "Lane 1",
            "type": "Lane",
            "category": "lane",
            "isGroup": "true",
            "loc": "-435.9133936523437 -205.05991240234377",
            "size": "766 310.86631745643615"
        }, {
            "key": "K_5",
            "text": "Start",
            "type": "StartEvent",
            "category": "event",
            "group": "K_3",
            "output": [],
            "loc": "-386.91339365234376 -153.55991240234377"
        }, {
            "key": "K_8",
            "text": "Выписка",
            "type": "UserTask",
            "category": "task",
            "group": "K_3",
            "input": [],
            "output": [],
            "loc": "-118.91339365234376 -146.55991240234377"
        }, {
            "key": "K_9",
            "text": "Направление",
            "type": "UserTask",
            "category": "task",
            "group": "K_3",
            "input": [],
            "output": [],
            "loc": "-101.36149365234382 -17.094724902343813"
        }, {
            "key": "gateway2",
            "text": "Exclusive",
            "type": "ExclusiveGateway",
            "category": "gateway",
            "group": "K_3",
            "loc": "-292.61287619590763 -139.45908869590764"
        }, {
            "key": "event2",
            "text": "End",
            "type": "EndEvent",
            "category": "event",
            "group": "K_3",
            "input": [],
            "loc": "3.970648804092434 -156.47617619590764"
        }, {
            "key": "K_19",
            "text": "End",
            "type": "EndEvent",
            "category": "event",
            "group": "K_3",
            "input": [],
            "loc": "200.8826613040925 -3.3223886959075912"
        }],
    "linkDataArray": [{
            "fromPort": "",
            "from": "event",
            "to": "task",
            "type": "SequenceFlow",
            "key": "K_1",
            "points": [-371.7064311523438, -354.87003740234377, -361.7064311523438, -354.87003740234377, -355.9564311523438, -354.87003740234377, -355.9564311523438, -347.87003740234377, -350.2064311523438, -347.87003740234377, -330.2064311523438, -347.87003740234377]
        }, {
            "fromPort": "",
            "from": "task",
            "to": "event6",
            "type": "SequenceFlow",
            "key": "K_4",
            "points": [-210.20643115234378, -347.87003740234377, -200.20643115234378, -347.87003740234377, -180.45643115234378, -347.87003740234377, -180.45643115234378, -341.87003740234377, -160.70643115234378, -341.87003740234377, -140.70643115234378, -341.87003740234377]
        }, {
            "fromPort": "",
            "from": "K_5",
            "to": "gateway2",
            "type": "SequenceFlow",
            "key": "K_7",
            "points": [-365.41339365234376, -153.55991240234377, -355.41339365234376, -153.55991240234377, -354.26313492412567, -153.55991240234377, -354.26313492412567, -139.45908869590764, -353.11287619590763, -139.45908869590764, -333.11287619590763, -139.45908869590764]
        }, {
            "fromPort": "",
            "from": "gateway2",
            "to": "K_8",
            "type": "SequenceFlow",
            "key": "K_10",
            "points": [-252.11287619590763, -139.45908869590764, -242.11287619590763, -139.45908869590764, -220.5131349241257, -139.45908869590764, -220.5131349241257, -146.55991240234377, -198.91339365234376, -146.55991240234377, -178.91339365234376, -146.55991240234377]
        }, {
            "fromPort": "",
            "from": "K_8",
            "to": "event2",
            "type": "SequenceFlow",
            "key": "K_14",
            "points": [-58.91339365234376, -146.55991240234377, -48.91339365234376, -146.55991240234377, -43.97137242412566, -146.55991240234377, -43.97137242412566, -156.47617619590764, -39.029351195907566, -156.47617619590764, -19.029351195907566, -156.47617619590764]
        }, {
            "fromPort": "",
            "from": "gateway2",
            "to": "K_9",
            "type": "SequenceFlow",
            "key": "K_11",
            "points": [-252.11287619590763, -139.45908869590764, -242.11287619590763, -139.45908869590764, -211.73718492412573, -139.45908869590764, -211.73718492412573, -17.094724902343813, -181.36149365234382, -17.094724902343813, -161.36149365234382, -17.094724902343813]
        }, {
            "fromPort": "",
            "from": "K_9",
            "to": "K_19",
            "type": "SequenceFlow",
            "key": "K_12",
            "points": [-41.361493652343825, -17.094724902343813, -31.361493652343825, -17.094724902343813, 63.260583825874335, -17.094724902343813, 63.260583825874335, -3.3223886959075912, 157.8826613040925, -3.3223886959075912, 177.8826613040925, -3.3223886959075912]
        }, {
            "from": "event6",
            "to": "K_5",
            "type": "MessageFlow",
            "category": "msg",
            "key": "K_6",
            "points": [-119.20643115234378, -320.37003740234377, -119.20643115234378, -310.37003740234377, -119.20643115234378, -252.71497490234378, -386.91339365234376, -252.71497490234378, -386.91339365234376, -195.05991240234377, -386.91339365234376, -175.05991240234377]
        }]
};
       return ret;
    }
    
});