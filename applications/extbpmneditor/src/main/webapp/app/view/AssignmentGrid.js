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

Ext.define('BPMN.view.AssignmentGrid', {
    extend: 'Ext.grid.Panel',
    alias: 'widget.BPMN-AssignmentGrid',
    columnLines: true,
    plugins: [Ext.create('Ext.grid.plugin.CellEditing', {
            clicksToEdit: 1
        })],
    initComponent: function () {
        var self = this;
        var myData = [];




        this.tbar = [{
                text: "Добавить",
                scope: this,
                handler: this.onAddClick
            }];

        this.store = Ext.create('Ext.data.Store', {
            fields: [
                {name: 'from'},
                {name: 'to'}
            ],
            data: myData
        });
        this.columns = [
            {text: "from",
                dataIndex: 'from',
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
    },
    onAddClick: function () {
        var sel = this;
        var store = this.store;
        var row = store.createModel({})
        //new store.model();
        store.insert(0, row);

        this.getStore().insert(0, row);
    },
    onRemoveClick: function (grid, rowIndex) {
        this.getStore().removeAt(rowIndex);
    },
    getValue: function () {
        var store = this.getStore();

        var data = [];
        store.each(function (rec){
            var row={from:rec.get("from"),to:rec.get('to')};
            data.push(row);
        });
        return data;
    },
    setValue:function(o){
       var store = this.getStore();
       store.removeAll();
       store.setData(o);
    }

});

