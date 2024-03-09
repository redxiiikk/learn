package main

import (
	"gorm.io/gorm"
	"gorm.io/gorm/clause"
)

type EntityDefine struct {
	gorm.Model

	Key  string
	Name string

	Attributes []AttributeDefine `gorm:"foreignKey:EntityDefineId"`
}

type AttributeDefine struct {
	gorm.Model

	EntityDefineId uint
	Key            string
	Name           string
	Type           string
	DefaultValue   string
}

type Entity struct {
	gorm.Model

	DefineId uint
}

type Attribute struct {
	gorm.Model

	EntityId uint
	DefineId uint
	Value    string
}

func (EntityDefine) TableName() string {
	return "t_define_entity"
}

func (AttributeDefine) TableName() string {
	return "t_define_attribute"
}

func (Entity) TableName() string {
	return "t_define"
}

func (Attribute) TableName() string {
	return "t_attribute"
}

func QueryEntityDefine(db *gorm.DB, id uint) EntityDefine {
	var entityDefine EntityDefine
	db.Model(&EntityDefine{}).Preload(clause.Associations).Find(&entityDefine, id)
	return entityDefine
}

func QueryAllEntityDefine(db *gorm.DB) []EntityDefine {
	var entityDefines []EntityDefine
	db.Model(&EntityDefine{}).Preload(clause.Associations).Find(&entityDefines)
	return entityDefines
}
