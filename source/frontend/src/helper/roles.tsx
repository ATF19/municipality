import { UserDto } from "../rest";

const haveMunicipalityRole = (user: UserDto): Boolean => 
    Array.from(user.municipalitiesResponsible).length > 0 || Array.from(user.municipalitiesAuditor).length > 0


const haveDistrictRole = (user: UserDto): Boolean => 
    Array.from(user.districtsResponsible).length > 0 || Array.from(user.districtsAuditor).length > 0

const canSeeMunicipalities = (user: UserDto): Boolean => {
    if (user.isAdmin)
        return true;
    
    return haveMunicipalityRole(user);
}

const canSeeDistricts = (user: UserDto): Boolean => {
    if (canSeeMunicipalities(user))
        return true;
    
    return haveDistrictRole(user);
}

const canSeeComplaints = (user: UserDto): Boolean => haveMunicipalityRole(user) || haveDistrictRole(user);

const userHomePage = (user?: UserDto) => {
    if (!user)
        return "404";

    if (user.isAdmin)
        return "arrondissements";

    if (haveMunicipalityRole(user) || haveDistrictRole(user))
        return "plaintes";

    return "404";
}

export {userHomePage, haveMunicipalityRole, haveDistrictRole, canSeeMunicipalities, canSeeDistricts, canSeeComplaints}