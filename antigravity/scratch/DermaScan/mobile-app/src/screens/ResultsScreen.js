import React from 'react';
import { View, Text, Button, StyleSheet } from 'react-native';

export default function ResultsScreen({ navigation }) {
    return (
        <View style={styles.container}>
            <Text style={styles.title}>Analysis Results</Text>
            <Text>No data yet.</Text>
            <Button
                title="Back to Home"
                onPress={() => navigation.navigate('Home')}
            />
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center',
    },
    title: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 20,
    },
});
