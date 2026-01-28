import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
init() {
        PicPayApplicationKt.doInitKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
            .ignoresSafeArea(.all)
        }
    }
}