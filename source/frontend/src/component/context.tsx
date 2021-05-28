import { createContext } from "react";
import { UserDto } from "../rest";

type ContextType = {
    user?: UserDto,
    toggleGlobalLoading: (isLoading: boolean) => any
}

const Context = createContext<ContextType>({toggleGlobalLoading: (t) => t })

export default Context;