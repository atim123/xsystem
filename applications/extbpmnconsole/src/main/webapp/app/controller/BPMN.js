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

Ext.define('BPMN.controller.BPMN', {
    extend: 'Ext.app.Controller',
    views: ['BPMN.view.ProcessInstansesPanel',
        'BPMN.view.TemplatesPanel',
        'BPMN.view.TaskListPanel',
        'BPMN.view.StartDlg',
        'BPMN.view.ContextDlg'

    ],
    refs: [{ref: 'templates', selector: 'BPMN-templatesPanel'}
        , {ref: 'proceses', selector: 'BPMN-processInstansesPanel'}
        , {ref: 'taskList', selector: 'BPMN-taskListPanel'}
        , {ref: 'timerList', selector: 'BPMN-timerListPanel'}
    ],
    init: function () {
        this.control({
            'BPMN-templatesPanel button[action=uploaddefinition]': {
                click: this.startUploadDefinition
            },
            'BPMN-processInstansesPanel button[action=update]': {
                click: this.refreshProceses
            },
            'BPMN-templatesPanel button[action=update]': {
                click: this.refreshTemplates
            },
            'BPMN-taskListPanel button[action=update]': {
                click: this.refreshTaskList
            },
            'BPMN-timerListPanel button[action=update]': {
                click: this.refreshTimerList
            },
            'BPMN-timerListPanel button[action=timerStart]': {
                click: this.timerStart
            },

            'BPMN-timerListPanel button[action=timerStop]': {
                click: this.timerStop
            },

            'BPMN-startDlg button[action=runProc]': {
                click: this.startProces
            },
            'BPMN-startDlg button[action=compliteTask]': {
                click: this.compliteTask
            }

        });
        this.application.on({
            startproces: {
                fn: this.runStartProces,
                scope: this
            }
        });
        this.application.on({
            stopproces: {
                fn: this.stopProces,
                scope: this
            }
        });
        this.application.on({
            complitetask: {
                fn: this.runCompliteTask,
                scope: this
            }
        });
        this.application.on({
            processcontext: {
                fn: this.runProcessContext,
                scope: this
            }
        });

        this.application.on({
            pushtimer: {
                fn: this.pushTimer,
                scope: this
            }
        });



        this.application.on({LoadTemplate: {
                fn: this.uploadDefinition,
                scope: this
            }
        });

        this.application.on({checkTimer: {
                fn: this.checkTimer,
                scope: this
            }
        });

        this.application.on({loadfromdb: {
                fn: this.loadFromDB,
                scope: this
            }
        });

        this.application.on({undeploydefinitions: {
                fn: this.undeployDefinitions,
                scope: this
            }
        });

    },

    startUploadDefinition: function () {
        var dlg = Ext.create('BPMN.view.DefListDlg', {});
        dlg.show();
        /*  var self=this;
         Ext.Msg.prompt('Шаблон', 'Имя шаблона:', function(btn, text){
         if (btn === 'ok'){
         self.uploadDefinition(text);
         }
         });*/

    },

    uploadDefinition: function (defName) {
        var self = this;
        var url = "rest/db/repo/loaddef/" + defName;
        Ext.Ajax.request({
            url: url,
            jsonData: {},
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('OK', text);
                self.refreshTemplates();
                self.refreshProceses();
                self.refreshTaskList();
            },
            failure: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('Failure', text);
            }
        });
    },

    refreshProceses: function () {
        var grid = this.getProceses();
        var store = grid.getStore();
        store.load();
    },
    refreshTemplates: function () {
        var grid = this.getTemplates();
        var store = grid.getStore();
        store.load();
    },
    refreshTaskList: function () {
        var grid = this.getTaskList();
        var store = grid.getStore();
        store.load();

    },

    refreshTimerList: function () {
        var grid = this.getTimerList();
        var store = grid.getStore();
        store.load();
        this.checkTimer();
    },

    runStartProces: function (record) {
        var dlg = Ext.create('BPMN.view.StartDlg', {procId: record.get("id"), doAction: "runProc"});
        dlg.show();
    },

    runCompliteTask: function (record) {
        var id = record.get("id");
        this.takeTaskAjax(id);
       
    },

    takeTaskAjax: function (id) {
        var self = this;
        var url = "rest/db/repo/task/take/" + id;
        Ext.Ajax.request({
            url: url,
            jsonData: {},
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;
                var jsonData = self.getJsonObject(text);
                var formKey = jsonData.data.formKey;
                var formContext = jsonData.data.formContext;
                var strData = "";
                if (formContext) {
                    strData = Ext.encode(formContext);
                }
                var dlg = Ext.create('BPMN.view.StartDlg', {taskId: id, doAction: "compliteTask"});
                dlg.setTitle("Выполнить -" + formKey);
                dlg.setData(strData);
                dlg.show();
                self.refreshProceses();
                self.refreshTaskList();
            },
            failure: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('Failure', text);
            }
        });
    },

    runProcessContext: function (processid) {
        var self = this;
        var url = "rest/db/repo/processinstanses/context/" + processid;
        Ext.Ajax.request({
            url: url,
            jsonData: {},
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;
                var jsonData = self.getJsonObject(text);
                var data = jsonData.data;
                var strData = ""
                if (data) {
                    strData = JSON.stringify(data, null, '\t')
                    //    Ext.encode (data); 
                }
                var dlg = Ext.create('BPMN.view.ContextDlg', {});
                dlg.setTitle("Контекст-" + processid);
                dlg.setData(strData);
                dlg.show();
                //     dlg.setData(strData);
                self.refreshProceses();
                self.refreshTaskList();
                self.refreshTimerList();
            },
            failure: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('Failure', text);
            }
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
                    alert("Ожидается обьект");
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

    startProces: function (button) {
        var win = button.up('window');
        var win = button.up('window');
        var valueTxt = win.getData();

        var procId = win.procId;
        var data = this.getJsonObject(valueTxt);
        if (data) {
            win.close();
            this.startProcesAjax(procId, data);
        }
    },

    startProcesAjax: function (id, data) {
        var self = this;
        var url = "rest/db/repo/start/" + id;
        Ext.Ajax.request({
            url: url,
            jsonData: data,
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('OK', text);
                self.refreshProceses();
                self.refreshTaskList();
                self.refreshTimerList();
            },
            failure: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('Failure', text);
            }
        });
    },
    stopProces: function (record) {
        var self = this;
        var url = "rest/db/repo/stop/" + record.get("id");
        Ext.Ajax.request({
            url: url,
            jsonData: {},
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('OK', text);
                self.refreshProceses();
                self.refreshTaskList();
                self.refreshTimerList();
            },
            failure: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('Failure', text);
            }
        });
    },

    compliteTask: function (button) {
        var win = button.up('window');
        var valueTxt = win.getData();
        var taskId = win.taskId;

        var data = this.getJsonObject(valueTxt);
        if (data) {
            win.close();
            this.compliteTaskAjax(taskId, data);
        }
    },

    compliteTaskAjax: function (id, data) {
        var self = this;
        var url = "rest/db/repo/task/complite/" + id;
        Ext.Ajax.request({
            url: url,
            jsonData: data,
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('OK', text);
                self.refreshProceses();
                self.refreshTaskList();
                self.refreshTimerList();
            },
            failure: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('Failure', text);
            }
        });
    },

    pushTimer: function (record) {
        var self = this;
        var url = "rest/db/repo/timer/push/" + record.get("id");
        Ext.Ajax.request({
            url: url,
            jsonData: {},
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;

                Ext.Msg.alert('OK', text);
                self.refreshProceses();
                self.refreshTaskList();
                self.refreshTimerList();
            },
            failure: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('Failure', text);
            }
        });
    },

    loadFromDB: function (record) {

        var id = record.get('id');
        var url = "rest/db/repo/downloaddef/" + id;
        Ext.Ajax.request({
            url: url,
            jsonData: {},
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('OK', text);

            },
            failure: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('Failure', text);
            }
        });
    },

    undeployDefinitions: function (record) {
        var id = record.get('id');
        var self = this;
        var url = "rest/db/repo/undeploydefinitions/" + id;
        Ext.Ajax.request({
            url: url,
            jsonData: {},
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('OK', text);
                self.refreshTemplates();
                self.refreshProceses();
                self.refreshTaskList();
                self.refreshTimerList();
            },
            failure: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('Failure', text);
            }
        });
    },

    timerStart: function (button) {
        var panel = button.up('BPMN-timerListPanel');
        this.timerStartStopAjax("start", panel);
    },

    timerStop: function (button) {
        var panel = button.up('BPMN-timerListPanel');
        this.timerStartStopAjax("stop", panel);
    },
    // start  stop

    checkTimer: function () {
        var panel = this.getTimerList();
        this.timerStartStopAjax("isworking", panel);
    },

    timerStartStopAjax: function (cmd, panel) {
        var self = this;
        var buttonStart = panel.down('button[action=timerStart]');
        var buttonStop = panel.down('button[action=timerStop]');
        var url = "rest/db/repo/timer/" + cmd;

        Ext.Ajax.request({
            url: url,
            jsonData: {},
            method: 'POST',
            success: function (response, opts) {
                var text = response.responseText;
                var data = self.getJsonObject(text);
                buttonStart.setDisabled(data.data);
                buttonStop.setDisabled(!data.data);

                //Ext.Msg.alert('OK', text);

            },
            failure: function (response, opts) {
                var text = response.responseText;
                Ext.Msg.alert('Failure', text);
            }
        });
    }

});

