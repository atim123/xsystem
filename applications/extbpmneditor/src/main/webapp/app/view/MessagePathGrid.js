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

Ext.define('BPMN.view.MessagePathGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.BPMN-MessagePathGrid',
    columnLines: true,
    plugins: [Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1
        })],
    initComponent: function () {
        var self = this;
        var myData = [];

        this.tbar = [{
                text: "Добавить",
                action: "add",
                disabled: true,
                scope: this,
                handler: this.onAddClick
            }];

        this.store = Ext.create('Ext.data.Store', {
            fields: [
                {name: 'pathid'},
                {name: 'to'}
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
            {//text: '',
                width: 30,
                menuDisabled: true,
                xtype: 'actioncolumn',
                align: 'center',
                icon: 'images/delete.png',
                scope: this,
                handler: this.onRemoveClick
            }];

        this.callParent(arguments);

        this.on('validateedit', function (editor, context) {
            if (context.field === "pathid") {
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
                if (context.field === "pathid") {
                    var newValue = context.value.trim();
                    self.owner.changeMessagePathKey(self.mesage,context.originalValue,newValue);
                } else if (context.field==="to"){
                    var pathKey=context.record.get("pathid");
                    var newValue = context.value.trim();
                    self.owner.setMessagePathValue(self.mesage,pathKey,newValue);
                }
            }
        });
    },
    onAddClick: function () {
        if (this.mesage) {
            var sel = this;
            var store = this.getStore();
            var row = sel.owner.newMessagePath(sel.mesage, store);
            store.insert(0, row);
            row.commit();
        }
    },
    onRemoveClick: function (grid, rowIndex) {
        var store=grid.getStore();//.removeAt(rowIndex);
        var row =store.getAt(rowIndex);
        var pathKey=row.get("pathid");
        this.owner.removeMessagePath(this.mesage,pathKey);
        store.removeAt(rowIndex);
    }

});



