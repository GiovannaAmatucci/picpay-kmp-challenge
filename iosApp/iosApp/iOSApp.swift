import SwiftUI
import ComposeApp

@main
struct iOSApp: App {
init() {
        IosApplicationKt.doInitKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
            .ignoresSafeArea(.all)
        }
    }
}