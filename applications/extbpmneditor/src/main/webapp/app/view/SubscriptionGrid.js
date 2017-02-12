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

Ext.define('BPMN.view.SubscriptionGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.BPMN-SubscriptionGrid',
    columnLines: true,
    plugins: [Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1
        })],
    initComponent: function () {
        var self=this;
        // this.inspectedObject=this.getInspectedObject();
        var myData = this.buildGridData();

        this.store = Ext.create('Ext.data.Store', {
            fields: [
                {name: 'pathid'},
                {name: 'to'},
                {name: 'from'}

            ],
            data: myData
        });

        this.columns = [
            {text: "id",
                dataIndex: 'pathid',
                flex: 1,
                editor: {
                    xtype: 'textfield'
                }
            },
            {text: "to",
                dataIndex: 'to',
                flex: 1,
                editor: {
                    xtype: 'textfield'
                }
            },
            {text: "from",
                dataIndex: 'from',
                flex: 1,
                editor: {
                    xtype: 'textfield'
                }
            }];

        this.callParent(arguments);

        this.on('validateedit', function (editor, context) {
            if (context.field === "pathid" || context.field === "to") {
                context.cancel = true;
                return false;
            };
            return true;
        });
        
        this.on('edit', function (editor, context) {
            context.record.commit();
            if (context.originalValue !== context.value) {
                if (context.field === "from") {
                    var newValue = context.value.trim();
                    var record=context.record;
                    var rowIdx=context.rowIdx;
                    var key=record.get("pathid");
                    self.setSubscription(rowIdx,key,newValue);
                }
            }
        });
        
    },
    setSubscription: function (indx,key,value) {
        var subscription = this.subscription;
        var o=subscription[indx];
        o[key]=value;
        return subscription;
    },
    
    buildGridData: function () {
        var ret = [];
        var modelData = this.inspectedObject.data;
        if (modelData.message) {
            var mes = modelData.message;
            var prosess = this.getProsess(modelData.group);

            ret = this.makeSubscription(prosess, mes);
        }
        return ret;
    },
    getProsess: function (groupKey) {
        var diagram = this.inspectedObject.diagram;
        var ret = diagram.model.getProsess(groupKey);
        return ret;
    },
    getMessage: function (mes) {
        var diagram = this.inspectedObject.diagram;
        var ret = diagram.model.getMessage(mes);
        return ret;
    },
    
    
    makeSubscription: function (prosess, mes) {
        var subscriptions = prosess.subscriptions || (prosess.subscriptions = {});
        var subscription = subscriptions[mes] || (subscriptions[mes] = []);
        this.subscription = subscription;
        var message = this.getMessage(mes);
        var ret = [];
        if (message) {
            var messagePath = message.messagePath;
            for (var path in messagePath) {
                if (messagePath.hasOwnProperty(path)) {
                    var key = path;
                    var ok = subscription.find(function (element) {
                        var ret = element.hasOwnProperty(key);
                        return ret;
                    });
                    if (!ok) {
                        var row = {};
                        row[key] = "";
                        subscription.push(row);
                    }
                }
            }
            this.subscription.forEach(function (item) {
                var key = Object.keys(item)[0];
                var mesageV = messagePath[key];
                var contextV = item[key];
                var row = {pathid: key, to: mesageV, from: contextV};
                ret.push(row);
            });
        }

        return ret;
    }
    //isSubscription:function(element, index, array){ 
    //   
    //}

});

