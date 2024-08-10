package com.chub.officemanager.data.repo

import com.chub.officemanager.data.relations.ItemWithRelations
import com.chub.officemanager.util.OfficeItem
import javax.inject.Inject

class OfficeItemMapper @Inject constructor(): Mapper<ItemWithRelations, OfficeItem> {
    override fun map(
        input: ItemWithRelations,
    ): OfficeItem {
        val item = input.item.item
        return OfficeItem(
            item.itemId,
            item.name,
            item.description,
            input.item.type,
            input.relations.map { relation ->
                val item = relation.item
                OfficeItem(
                    item.itemId,
                    item.name,
                    item.description,
                    relation.type
                )
            }
        )
    }
}