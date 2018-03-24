const electron = require('electron')
// Module to control application life.
const app = electron.app
// Module to create native browser window.
const BrowserWindow = electron.BrowserWindow
// var Menu = require('menu');
// var MenuItem = require('menu-item');
const path = require('path')
const url = require('url')

const {Menu, MenuItem} = require('electron')
// var sleep = require('sleep');


// Keep a global reference of the window object, if you don't, the window will
// be closed automatically when the JavaScript object is garbage collected.
let mainWindow


// This method will be called when Electron has finished
// initialization and is ready to create browser windows.
// Some APIs can only be used after this event occurs.
app.on('ready', createWindow);

// Quit when all windows are closed.
app.on('window-all-closed', function () {
    // On OS X it is common for applications and their menu bar
    // to stay active until the user quits explicitly with Cmd + Q
    if (process.platform !== 'darwin') {
        app.quit()
    }
});

app.on('activate', function () {
    // On OS X it's common to re-create a window in the app when the
    // dock icon is clicked and there are no other windows open.
    if (mainWindow === null) {
        createWindow()
    }
});

function createWindow() {
    var childProcess = require('child_process');
    var options = {maxBuffer: 1024 * 1024 * 100, encoding: 'utf8', timeout: 0};
    var jarPath = "java -jar " + __dirname + "/lib/siber.jar";
    console.log("jarPath is " + jarPath);
    // var child = childProcess.exec('java -jar /siber.jar', options, function (error, stdout, stderr) {
    var child = childProcess.exec(jarPath, options, function (error, stdout, stderr) {
        if (error) {
            console.log(error.stack);
            console.log('Error Code: ' + error.code);
            console.log('Error Signal: ' + error.signal);
        }
        console.log('Results: \n' + stdout);
        if (stderr.length) {
            console.log('Errors: ' + stderr);
        }
    });

    // Create the browser window.
    mainWindow = new BrowserWindow({width: 960, height: 800, minWidth: 960, minHeight: 800});

    setTimeout(function() {
            console.log("setTimeout done");
            mainWindow.loadURL(url.format({
                pathname: path.join(__dirname, 'build/index.html'),
                protocol: 'file:',
                slashes: true
            }));
    }, 5000);

    // Open the DevTools.
    // mainWindow.webContents.openDevTools();

    // Emitted when the window is closed.
    mainWindow.on('closed', function () {
        // Dereference the window object, usually you would store windows
        // in an array if your app supports multi windows, this is the time
        // when you should delete the corresponding element.
        child.kill();
        console.log('close mainWindow!!');
        mainWindow = null
    });

    // Create the Application's main menu
    var template = [{
        label: 'Electron',
        submenu: [
            {
                label: 'About Electron',
                selector: 'orderFrontStandardAboutPanel:'
            },
            {
                type: 'separator'
            },
            {
                label: 'Services',
                submenu: []
            },
            {
                type: 'separator'
            },
            {
                label: 'Hide Electron',
                accelerator: 'Command+H',
                selector: 'hide:'
            },
            {
                label: 'Hide Others',
                accelerator: 'Command+Shift+H',
                selector: 'hideOtherApplications:'
            },
            {
                label: 'Show All',
                selector: 'unhideAllApplications:'
            },
            {
                type: 'separator'
            },
            {
                label: 'Quit',
                accelerator: 'Command+Q',
                click: function () {
                    app.quit();
                }
            },
        ]
    },
        {
            label: 'Edit',
            submenu: [
                {
                    label: 'Undo',
                    accelerator: 'Command+Z',
                    selector: 'undo:'
                },
                {
                    label: 'Redo',
                    accelerator: 'Shift+Command+Z',
                    selector: 'redo:'
                },
                {
                    type: 'separator'
                },
                {
                    label: 'Cut',
                    accelerator: 'Command+X',
                    selector: 'cut:'
                },
                {
                    label: 'Copy',
                    accelerator: 'Command+C',
                    selector: 'copy:'
                },
                {
                    label: 'Paste',
                    accelerator: 'Command+V',
                    selector: 'paste:'
                },
                {
                    label: 'Select All',
                    accelerator: 'Command+A',
                    selector: 'selectAll:'
                },
            ]
        },
        // {
        //     label: 'View',
        //     submenu: [
        //         {
        //             label: 'Reload',
        //             accelerator: 'Command+R',
        //             click: function() { BrowserWindow.getFocusedWindow().reloadIgnoringCache(); }
        //         },
        //         {
        //             label: 'Toggle DevTools',
        //             accelerator: 'Alt+Command+I',
        //             click: function() { BrowserWindow.getFocusedWindow().toggleDevTools(); }
        //         },
        //     ]
        // },
        {
            label: 'Window',
            submenu: [
                {
                    label: 'Minimize',
                    accelerator: 'Command+M',
                    selector: 'performMiniaturize:'
                },
                {
                    label: 'Close',
                    accelerator: 'Command+W',
                    selector: 'performClose:'
                },
                {
                    type: 'separator'
                },
                {
                    label: 'Bring All to Front',
                    selector: 'arrangeInFront:'
                },
            ]
        },
        {
            label: 'Help',
            submenu: []
        }];

    menu = Menu.buildFromTemplate(template);
    Menu.setApplicationMenu(menu);
    // var template = [{
    //     label: "Application",
    //     submenu: [
    //         { label: "About Application", selector: "orderFrontStandardAboutPanel:" },
    //         { type: "separator" },
    //         { label: "Quit", accelerator: "Command+Q", click: function() { app.quit(); }}
    //     ]}, {
    //     label: "Edit",
    //     submenu: [
    //         { label: "Undo", accelerator: "CmdOrCtrl+Z", selector: "undo:" },
    //         { label: "Redo", accelerator: "Shift+CmdOrCtrl+Z", selector: "redo:" },
    //         { type: "separator" },
    //         { label: "Cut", accelerator: "CmdOrCtrl+X", selector: "cut:" },
    //         { label: "Copy", accelerator: "CmdOrCtrl+C", selector: "copy:" },
    //         { label: "Paste", accelerator: "CmdOrCtrl+V", selector: "paste:" },
    //         { label: "Select All", accelerator: "CmdOrCtrl+A", selector: "selectAll:" }
    //     ]}
    // ];
    //
    // Menu.setApplicationMenu(Menu.buildFromTemplate(template));

};


// In this file you can include the rest of your app's specific main process
// code. You can also put them in separate files and require them here.
