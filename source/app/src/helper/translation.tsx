const translateStatus = (status: string): string => {
    switch(status) {
        case 'CREATED': 
            return 'جديد'
        case 'IN_PROGRESS': 
            return 'فعال'
        case 'SENT_TO_EXTERNAL_SERVICE': 
            return 'تم إرساله إلى المصلحة المختصة'
        case 'FINISHED': 
            return 'منجز'
        case 'REJECTED':
            return 'مرفوض'
        default:
            return ''
    }
}

export { translateStatus }