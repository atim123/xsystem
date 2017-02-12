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
Ext.define('BPMN.view.MessageGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.BPMN-MessageGrid',
    columnLines: true,
    plugins: [Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1
        })],
    initComponent: function () {
        var self = this;
        var myData = this.owner.getMessagesData();

        this.store = Ext.create('Ext.data.Store', {
            fields: [
                {name: 'msgid'},
                {name: 'name'}
            ],
            data: myData
        });

        this.tbar = [{
                text: "Добавить",
                scope: this,
                handler: this.onAddClick
            }];

        this.columns = [
            {
                // text: 'Исполнить',
                width: 40,
                menuDisabled: true,
                xtype: 'actioncolumn',
                align: 'center',
                icon: 'images/mark_active.png',
                handler: function (grid, rowIndex, colIndex, actionItem, event, record, row) {
                    var sm = grid.getSelectionModel();
                    var sr = [record];
                    sm.select(sr);
                }
            },
            {text: "id",
                dataIndex: 'msgid',
                flex: 1,
                editor: {
                    allowBlank: false,
                    xtype: 'textfield'
                }
            },
            {text: "name",
                dataIndex: 'name',
                flex: 1,
                editor: {
                    xtype: 'textfield'
                }
            },
            {//text: '',
                width: 30,
                menuDisabled: true,
                xtype: 'actioncolumn',
                align: 'center',
                icon: 'images/delete.png',
                scope: this,
                handler: this.onRemoveClick
            }];

        this.on('validateedit', function (editor, context) {
            if (context.field === "msgid") {
                var newValue = context.value;
                newValue = newValue.trim();
                if (newValue === "") {
                    context.cancel = true;
                    return false;
                }
                var oldValue=context.originalValue;
                if (newValue!==oldValue){
                    if (!self.owner.checkUniqueKey(newValue)){
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
            
                if (context.field === "msgid") {
                    var newValue = context.value.trim();
                    self.owner.changeMessageKey(context.originalValue,newValue);
                }
            }
        });

        this.callParent(arguments);

        this.getSelectionModel().on('selectionchange', function (sm, selectedRecord) {
            var msg;
            if (selectedRecord.length) {
                var row = selectedRecord[0];
                msg = row.get("msgid");
            }
            self.owner.getPathdata(msg);
        });
        // add custom events
        //this.addEvents('addmesage', 'updatemessage','deletemessage');

    },
    setData: function (modelData) {
        var messages = modelData.messages || (modelData.messages = {});
        var data = [];

    },
    onAddClick: function () {
        var sel = this;
        var store = this.getStore();
        var row = sel.owner.newMessage(store);
        store.insert(0, row);
        row.commit();
    },
    onRemoveClick: function (grid, rowIndex) {
       var store=grid.getStore();//.removeAt(rowIndex);
        var row =store.getAt(rowIndex);
        var mes=row.get("msgid");
        this.owner.removeMessage(mes);
        this.getStore().removeAt(rowIndex);
    }
});


