import React from 'react';
import { NavigationContainer } from "@react-navigation/native";
import { createStackNavigator, StackNavigationOptions } from "@react-navigation/stack";
import Home from "../screen/home";
import CreateComplaint from '../screen/createComplaint';
import Complaint from '../screen/complaint';
import ComplaintList from '../screen/complaintList';

const Stack = createStackNavigator();

const Router = () => (
    <NavigationContainer>
        <Stack.Navigator screenOptions={ScreenOptions} initialRouteName="Home">
            <Stack.Screen options={{title: "رئيسية", headerShown: false}} 
                name="Home" 
                component={Home} />
            <Stack.Screen options={{title: "شكوى"}} 
                name="CreateComplaint" 
                component={CreateComplaint} />
            <Stack.Screen options={{title: "شكوى"}} 
                name="Complaint" 
                component={Complaint} />
            <Stack.Screen options={{title: "متابعة الشكاوي"}} 
                name="ComplaintList" 
                component={ComplaintList} />
        </Stack.Navigator>
    </NavigationContainer>
)

const ScreenOptions: StackNavigationOptions = {
    headerStyle: {
        backgroundColor: '#2d3436',
    },
    headerTintColor: '#fff',
    headerTitleStyle: {
        fontWeight: 'bold',
    }
}

export default Router;