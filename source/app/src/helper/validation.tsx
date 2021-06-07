const validateEmail = (email: string) => {
    const re = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email.toLowerCase());
}

const validatePhone = (phone: string) => {
    let phoneWithoutSpace = phone.replace(/ /g,'');
    if (phoneWithoutSpace.length != 8)
        return false;
    return /^\d+$/.test(phoneWithoutSpace);
}

export { validateEmail, validatePhone }