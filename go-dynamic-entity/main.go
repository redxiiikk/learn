package main

import (
	"encoding/json"
	"github.com/glebarez/sqlite"
	"gorm.io/gorm"
)

func main() {
	db, err := gorm.Open(sqlite.Open("file::memory:?cache=shared"))
	if err != nil {
		panic("can't open database")
	}

	err = db.AutoMigrate(&EntityDefine{}, &AttributeDefine{}, &Entity{}, &Attribute{})
	if err != nil {
		panic("can't create table at database: " + err.Error())
	}
	defineCommodity(db)
	defineVendor(db)
	defines := QueryAllEntityDefine(db)
	for _, de := range defines {
		printByJson(de)
	}
}

func printByJson(commodityDefine any) {
	bytes, err := json.Marshal(commodityDefine)
	if err != nil {
		panic("can't convert struct object to json: " + err.Error())
	}
	println(string(bytes))
}

func defineCommodity(db *gorm.DB) uint {
	commodityEntityDefine := EntityDefine{
		Key:  "commodity",
		Name: "商品",
		Attributes: []AttributeDefine{
			{
				Key:          "type",
				Name:         "商品类型",
				Type:         "string",
				DefaultValue: "",
			},
			{
				Key:          "channel",
				Name:         "渠道",
				Type:         "string",
				DefaultValue: "",
			},
			{
				Key:          "unit-price",
				Name:         "单价",
				Type:         "number",
				DefaultValue: "",
			},
		},
	}

	db.Create(&commodityEntityDefine)
	return commodityEntityDefine.ID
}

func defineVendor(db *gorm.DB) uint {
	vendorEntityDefine := EntityDefine{
		Key:  "vendor",
		Name: "供应商",
		Attributes: []AttributeDefine{
			{
				Key:          "type",
				Name:         "商品类型",
				Type:         "string",
				DefaultValue: "",
			},
			{
				Key:          "channel",
				Name:         "渠道",
				Type:         "string",
				DefaultValue: "",
			},
			{
				Key:          "unit-price",
				Name:         "单价",
				Type:         "number",
				DefaultValue: "",
			},
		},
	}
	db.Create(&vendorEntityDefine)
	db.Create(&EntityDefine{
		Key:  "vendor-commodity",
		Name: "供应商商品列表",
		Attributes: []AttributeDefine{
			{
				Key:          "commodity-id",
				Name:         "商品 ID",
				Type:         "reference",
				DefaultValue: "",
			},
		},
	})
	return vendorEntityDefine.ID
}
