import { StyleProp } from "react-native";

const Styles: StyleProp<any> = {
    container: {
        flex: 1,
    },
    containerCenter: {
        flex: 1, 
        justifyContent: 'center', 
        alignItems: 'center'
    },
    coverImage: {
        resizeMode: 'contain',
        width: '100%',
        height: '88%',
    },
    containerText: {
        color: '#fff',
        shadowColor: "#000",
        shadowOffset: {
            width: 0,
            height: 1,
        },
        shadowOpacity: 0.18,
        shadowRadius: 1.00,
        elevation: 0
    },
    homeMenuContainer: {
        flex: 1,
        justifyContent: 'center', 
        alignItems: 'center',
        paddingTop: 20,
        width: "100%",
        height: "100%",
        backgroundColor: '#fff',
        marginTop: "-30%",
        borderTopLeftRadius: 35,
        borderTopRightRadius: 35,
        shadowColor: "#000",
        shadowOffset: {
            width: 0,
            height: -2,
        },
        shadowOpacity: 0.5,
        shadowRadius: 1.00,
        elevation: 0
    },
    homeMenuCard: {
        width: 140,
        height: 120,
        justifyContent: 'center', 
        alignItems: 'center'
    },
    homeMenuCardIcon: {
        width: 60,
        height: 60,
        marginBottom: 7
    },
    row: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'space-between'
    },
    rightBorderdCard: {
        paddingRight: 30,
        borderRightColor: '#ecf0f1',
        borderRightWidth: 1
    },
    besideRightBorderdCard: {
        paddingLeft: 30
    },
    bottomRow: {
        paddingTop: 10,
        borderTopWidth: 1,
        borderTopColor: '#ecf0f1',
        marginTop: 70
    },
    darkerBackground: {
        backgroundColor: '#f1f2f6'
    },
    addPhotoBox: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        paddingTop: 30,
        paddingBottom: 30
    },
    addPhotoIcon: {
        width: 70,
        height: 70
    },
    addPhotoText: {
        color: '#ced6e0',
        fontSize: 30
    },
    screenWithPadding: {
        padding: 15
    },
    mainButton: {
        backgroundColor: '#0984e3',
        borderColor: '#0984e3'
    },
    photoPreview: {
        width: 300,
        height: 300,
        resizeMode: "contain"
    },
    formElement: {
        marginTop: 30
    },
    formLabel: {
        textAlign: 'right',
        fontSize: 18,
        color: '#636e72',
        fontWeight: 'bold',
        marginBottom: 7
    },
    removePhotoBtn: {
        width: 80,
        height: 60,
        position: 'absolute',
        top: 0,
        right: 0,
        borderRadius: 0,
        borderBottomLeftRadius: 10,
        backgroundColor: 'rgba(45, 52, 54, .8)',
        borderWidth: 0
    },
    formDividerContainer: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'center'
    },
    formDividerText: {
        flex: 1,
    },
    formDivider: {
        flex: 1,
        backgroundColor: 'rgba(45, 52, 54, .1)'
    },
    toastContainerStyle: {
        justifyContent: 'center',
        alignItems: 'center',
        width: "90%",
        height: 70
    },
    toastTextStyle: {
        fontSize: 20
    },
    errorPageContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
    spinner: {
        borderColor: '#2d3436'
    },
    errorPageElementWithMarginTop: {
        marginTop: 20
    },
    noDataIcon: {
        width: 30, 
        height: 30
    },
    complaintCardContainer: {
        marginBottom: 20
    },
    complaintCardImage: {
        height: 220
    },
    flexRow: {
        flexDirection: 'row'
    },
    complaintStatus: {
        paddingTop: 15,
        paddingBottom: 15,
        paddingLeft: 25,
        paddingRight: 25,
        position: 'absolute',
        top: 0,
        right: 0,
        shadowColor: "#000",
        shadowOffset: {
            width: 0,
            height: 1,
        },
        shadowOpacity: 0.1,
        shadowRadius: 1.00,
        elevation: 0

    },
    rightText: {
        textAlign: 'right'
    },
    complaintPageImage: {
        height: 440
    },
    infoDivider: {
        marginTop: 5,
        marginBottom: 5
    },
    infoSpaceSeparation: {
        marginTop: 20,
        marginBottom: 20
    },
    articleSpaceSeparation: {
        marginTop: 20
    },
    infoIcon: {
        width: 100,
        height: 100
    },
    marginBottom: {
        marginBottom: 15
    }
}

export default Styles;