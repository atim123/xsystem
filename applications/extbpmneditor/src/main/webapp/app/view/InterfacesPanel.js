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

Ext.define('BPMN.view.InterfacesPanel', {
    extend: 'Ext.tree.Panel',
    requires: ['BPMN.model.OperationModel', 'BPMN.view.MbeansOpDlg'],
    alias: 'widget.BPMN-interfacesPanel',
    rootVisible: false,
    plugins: [Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1
        })],
    initComponent: function () {
        var self = this;

        this.tbar = [
            {
                text: 'Добавить',
                scope: this,
                handler: function () {
                    var dlg = Ext.create('BPMN.view.MbeansOpDlg', {width: 900
                        , height: 400
                        , cbSelecton: self.setOperations
                    });
                    dlg.show();

                }
            }
        ];

        this.store = Ext.create('Ext.data.TreeStore', {
            model: 'BPMN.model.OperationModel',
            proxy: {
                type: 'memory',
                reader:{
                    type:"json",
                    rootProperty: "operations"
                }
               
            },
            root: {
                expanded: true,
                name: '.',
                operations: self.loadInterfaces()

                        //self.loadInterfaces()
            },
            lazyFill: true
        });

        this.columns = [
            {
                text: 'Удалить',
                width: 80,
                menuDisabled: true,
                xtype: 'actioncolumn',
                align: 'center',
                icon: 'images/delete.png',
                scope: self,
                handler: function (grid, rowIndex, colIndex, actionItem, event, record, row) {
                    var root = record.parentNode;
                    var type = record.get("type");
                    var id = record.get("id");
                    if (type === "interface") {
                        this.destroyInterface(id);
                    } else if (type === "operation") {
                        this.destroyOperation(id);
                    }
                    root.removeChild(record);
                }
            },
            {
                xtype: 'treecolumn', //this is so we know which column will show the tree
                text: 'ID',
                width: 200,
                sortable: true,
                dataIndex: 'id',
                editor: {
                    xtype: 'textfield'
                }
                // locked: true
            }, {
                text: 'Имя',
                flex: 1,
                dataIndex: 'name'
            }

        ];

        this.callParent(arguments);

        this.on('validateedit', function (editor, context) {
            if (context.field === "id") {
                var newValue = context.value;
                newValue = newValue.trim();
                if (newValue === "") {
                    context.cancel = true;
                    return false;
                }
                var oldValue = context.originalValue;
                if (newValue !== oldValue) {
                    if (!self.checkUniqueKey(newValue)) {
                        context.cancel = true;
                        return false;
                    }
                }

            }
            return true;
        });

        this.on('edit', function (editor, context) {
            context.record.commit();
            if (context.originalValue !== context.value) {
                if (context.field === "id") {
                    var newValue = context.value.trim();
                    self.changeInterfaceKey(context.originalValue, newValue);
                }
            }
        });


        this.setOperations = function (node) {
            var isInterface = node.data.type === "interface";
            var name = node.get("name");

            if (isInterface) {
                var operations = node.data.data;
                self.setInterfaceRow(name, operations);
            } else {
                var parentNode = node.parentNode;
                var interfacename = parentNode.get("name");
                var interface = self.setInterfaceRow(interfacename);
                self.setOperationRow(interface, name);
            }
        };



    },
    getSelectedOperation: function () {
        var row = this.getSelection();
         var ret;
        if (row && row.length > 0) {
            ret = row[0].get("id");
        }
        return ret;
       
    },
    loadInterfaces: function () {
        var diagram = this.inspectedObject.diagram;
        var model = diagram.model;
        var ret = model.getInterfaces();
        console.log(ret);
        return ret;
    },
    getUniqueKey: function () {
        var diagram = this.inspectedObject.diagram;
        var ret = diagram.model.makeUniqueKeyFunction(diagram.model);
        return ret;
    },
    checkUniqueKey: function (key) {
        var diagram = this.inspectedObject.diagram;
        var ret = diagram.model.checkUniqueKey(diagram.model, key);
        return ret;
    },
    changeInterfaceKey: function (oldkey, newKey) {
        var diagram = this.inspectedObject.diagram;
        diagram.model.changeInterfaceKey(oldkey, newKey);
    },
    destroyInterface: function (key) {
        var diagram = this.inspectedObject.diagram;
        diagram.model.destroyInterface(key);
    },
    destroyOperation: function (key) {
        var diagram = this.inspectedObject.diagram;
        diagram.model.destroyOperation(key);
    },
    createInterface: function (name) {
        var diagram = this.inspectedObject.diagram;
        var model = diagram.model;
        var ret = model.createInterface(name);
        return ret.id;
    },
    createOperation: function (interId, name) {
        var diagram = this.inspectedObject.diagram;
        var model = diagram.model;
        var ret = model.createOperation(interId, name);
        return ret.id;
    },
    consolePrintRoot: function (point, name) {
        var root = this.getRootNode();
        console.log(point);
        var internode = root.findChild('name', name, false);
        console.log(internode);

    },
    setInterfaceRow: function (name, operations) {
        var self = this;
        var root = this.getRootNode();


        var internode = root.findChild('name', name, false);

        if (!internode) {

            var key = self.createInterface(name);
            internode = root.appendChild({id: key, name: name, type: "interface"});

        }
        if (operations)
            operations.forEach(function (op) {
                self.setOperationRow(internode, op.name);
            });
        return internode;
    },
    setOperationRow: function (interfaceNode, operation) {
        var self = this;
        if (!interfaceNode.findChild('name', operation, false)) {
            var interId = interfaceNode.get("id");
            var key = self.createOperation(interId, operation);
            interfaceNode.appendChild({id: key, name: operation, leaf: true, type: "operation"});

        }
    }
});


