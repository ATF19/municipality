import React from 'react';
import * as eva from '@eva-design/eva';
import { EvaIconsPack } from '@ui-kitten/eva-icons';
import Router from './src/component/router';
import { ApplicationProvider, IconRegistry } from '@ui-kitten/components';
import { RootSiblingParent } from 'react-native-root-siblings';

const App = () => (
    <RootSiblingParent>
        <IconRegistry icons={EvaIconsPack} />
        <ApplicationProvider {...eva} theme={eva.light}>
            <Router />
        </ApplicationProvider>
    </RootSiblingParent>
)
export default App;