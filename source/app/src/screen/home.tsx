import { Icon, Layout } from "@ui-kitten/components";
import React from "react";
import { View, ImageBackground, TouchableHighlight } from "react-native";
import { Text } from '@ui-kitten/components';
import Styles from "../style";
import { TouchableOpacity } from "react-native-gesture-handler";

const Home = ({navigation}: any) => (
    <Layout style={Styles.container}>
      <View>
        <ImageBackground
          style={Styles.coverImage}
          source={require('../img/cover.png')}
        >
          <View style={Styles.containerCenter}>
            <Text category='h1' style={Styles.containerText}>مع بعضنا</Text>
            <Text category='h1' style={Styles.containerText}>نحسنو حومنا.</Text>
          </View>
        </ImageBackground>
        <View style={Styles.containerCenter}>
          <View style={Styles.homeMenuContainer}>

            <View style={[Styles.row]}>
              <View style={[Styles.homeMenuCard, Styles.rightBorderdCard]}>
                <TouchableOpacity onPress={() => navigation.navigate('CreateComplaint')}>
                  <View style={Styles.containerCenter}>
                    <Icon
                      style={Styles.homeMenuCardIcon}
                      fill='#74b9ff'
                      name='plus-square'
                    />
                    <Text category='h5' style={{color: '#0984e3'}}>قدم شكوى</Text>
                  </View>
                </TouchableOpacity>
              </View>

              <View style={[Styles.homeMenuCard, Styles.besideRightBorderdCard]}>
                <TouchableOpacity onPress={() => alert("x")}>
                  <View style={Styles.containerCenter}>
                    <Icon
                      style={Styles.homeMenuCardIcon}
                      fill='#81ecec'
                      name='folder'
                    />
                    <Text category='h5' style={{color: '#00cec9'}}>تابع شكواك</Text>
                  </View>
                </TouchableOpacity>
              </View>
            </View>

            <View style={[Styles.row, Styles.bottomRow]}>
              <View style={[Styles.homeMenuCard, Styles.rightBorderdCard]}>
                <TouchableOpacity onPress={() => alert("x")}>
                  <View style={Styles.containerCenter}>
                    <Icon
                      style={Styles.homeMenuCardIcon}
                      fill='#a29bfe'
                      name='calendar'
                    />
                    <Text category='h5' style={{color: '#6c5ce7'}}>جديد</Text>
                  </View>
                </TouchableOpacity>
              </View>

              <View style={[Styles.homeMenuCard, Styles.besideRightBorderdCard]}>
                <TouchableOpacity onPress={() => alert("x")}>
                  <View style={Styles.containerCenter}>
                    <Icon
                      style={Styles.homeMenuCardIcon}
                      fill='#fab1a0'
                      name='info'
                    />
                    <Text category='h5' style={{color: '#e17055'}}>معلومات</Text>
                  </View>
                </TouchableOpacity>
              </View>
            </View>

          </View>
        </View>
      </View>
    </Layout>
)

export default Home;