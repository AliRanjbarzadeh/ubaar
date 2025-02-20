package ir.ubaar.employeetask.data.interfaces


interface ResponseObject<out DomainObject : Any?> {
    fun toDomain(): DomainObject
}

