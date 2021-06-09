const statusBackground = (status: string) => {
    let bgColor = 'rgba(45, 52, 54, .8)'
    let color = '#fff'
    switch(status) {
        case 'CREATED': 
            bgColor = '#fff7e6'
            color = '#d46b08'
            break;

        case 'IN_PROGRESS': 
            bgColor = '#fffbe6'
            color = '#d48806'
            break;

        case 'SENT_TO_EXTERNAL_SERVICE': 
            bgColor = '#fff0f6'
            color = '#c41d7f'
            break;

        case 'FINISHED': 
            bgColor = '#b7eb8f'
            color = '#389e0d'
            break;

        case 'REJECTED':
            bgColor = '#fff1f0'
            color = '#cf1322'
            break;

    }
    return {
        backgroundColor: bgColor,
        color
    }
}

export { statusBackground } 