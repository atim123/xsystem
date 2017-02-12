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
Ext.define('BPMN.view.MbeansOpPanel', {
    extend: 'Ext.tree.Panel',
    requires: ['BPMN.model.MbeansOpTree'],
    alias: 'widget.BPMN-mbeansOpPanel',
    
    rootVisible: false,
    statics: {
       staticSearch:""
    },
    
    initComponent: function () {
        var self=this;
        var searchFld= Ext.create('Ext.form.field.Text',{flex: 1})
        searchFld.setValue(BPMN.view.MbeansOpPanel.staticSearch);
        
        this.tbar = [
            {
                text: 'Выбрать',
                scope: this,
                handler:function(){
                    var searchValue=searchFld.getValue();
                    BPMN.view.MbeansOpPanel.staticSearch=searchValue;
                    searchFld.setValue(self.staticSearch);
                    self.store.load({params:{search:searchValue}});
  
                }
            },
            searchFld
            
        ];


        this.store = Ext.create('Ext.data.TreeStore', {
            model: 'BPMN.model.MbeansOpTree',
            proxy: {
                type: 'ajax',
                url: 'jmxconsole/list',
                reader: {
                    type: 'json',
                    rootProperty: 'data'
                }
            },
            root: {
                expanded: true,
                text: '.'
            },
            lazyFill: true,
            autoLoad:false
        });

        this.columns = [{
                xtype: 'treecolumn', //this is so we know which column will show the tree
                text: 'Имя',
                width: 200,
                sortable: true,
                dataIndex: 'name',
                locked: true
            }, {
                text: 'Описание',
                flex: 1,
                dataIndex: 'description'
            }
        ];

        this.callParent(arguments);
        
    }
});




