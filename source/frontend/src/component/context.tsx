import { createContext } from "react";
import { UserDto } from "../rest";

type ContextType = {
    user?: UserDto,
    toggleGlobalLoading: (isLoading: boolean) => any,
    setLoggedinUser: (user: UserDto) => any
}

const Context = createContext<ContextType>({toggleGlobalLoading: (t) => t , setLoggedinUser: (t) => t})

export default Context;