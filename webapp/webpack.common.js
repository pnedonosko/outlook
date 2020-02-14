const path = require('path');

let config = {
    context: path.resolve(__dirname, '.'),
    // set the entry point of the application
    // can use multiple entry
    entry: {
        outlook: './src/main/webapp/react-app/index.tsx'
    },
    output: {
        filename: 'js/[name].bundle.js'
    },
    module: {
        rules: [
            {
                test: /\.css$/,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.less$/,
                use: ['style-loader', 'css-loader', 'less-loader']
            },
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/
            },
            // {
            //     test: /\.(js|jsx|tsx)$/,
            //     exclude: /node_modules/,
            //     use: [
            //         {
            //             loader: 'babel-loader',
            //             options: {
            //                 envName: 'development',
            //                 presets: ['@babel/preset-env', '@babel/preset-react', '@babel/preset-typescript']
            //             }
            //         }
            //     ]
            // }
        ]
    },
    resolve: {
        extensions: ['.tsx', '.ts', '.js', '.html']
    }
};

module.exports = config;
