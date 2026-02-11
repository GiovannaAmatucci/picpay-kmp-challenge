package com.giovanna.amatucci.desafio_android_picpay.util
object LogMessages {
    // Repository - Fluxo Principal
    const val REPO_CHECK_CACHE = "🔍 Verificando cache... Itens encontrados: %d"
    const val REPO_DECISION = "🤔 Decisão de Rede: ForceRefresh = %b | CacheVazio = %b -> Buscando? %b"

    // --- Repository: Fluxo de Rede ---
    const val REPO_NETWORK_START = "🚀 Buscando dados da API..."
    const val REPO_NETWORK_SUCCESS = "✅ API Sucesso: Atualizando banco local com %s itens"
    const val REPO_NETWORK_FAILURE = "⚠️ Falha no Refresh (Fallback ativo). Erro: %s"

    // --- Repository: Erros Graves ---
    const val REPO_CRITICAL_ERROR = "☠️ CRITICAL: Exceção não tratada capturada."
    const val REPO_ERROR_DATA = "Erro interno de processamento de dados"

    // --- ViewModel ---
    const val VM_INIT = "📱 ViewModel Inicializada"
    const val VM_PROCESS_START = "🔄 Iniciando loadUsers (isRefresh: %b)"
    const val VM_STATE_SUCCESS = "✨ Estado SUCCESS emitido | Itens: %d"
    const val VM_STATE_ERROR = "🔥 Estado ERROR emitido | Erro: %s"
    const val VM_STATE_LOADING_NETWORK = "🌐 Internet Conectada: Auto-refresh iniciado..."
    const val VM_STATE_NETWORK_FAILURE = "🔌 Internet Perdida"

    // API - Network Flows
    const val API_DATA_MAPPED = "API Success: Mapeamento de dados bem-sucedido"
    const val API_GET_USERS_ERROR = " Erro de servidor. Status: %d"
    const val API_GET_USERS_CLIENT_EXCEPTION = "Falha na requisição (ClientException): %s"
    const val API_GET_USERS_NETWORK_ERROR = "Sem conexão: Host desconhecido"
    const val API_GET_USERS_EXCEPTION = "Erro inesperado: %s"
}
object ErrorFormat {
    const val ERR_FORMAT_GENERIC = "Code: %d | Msg: %s"
    const val ERR_FORMAT_NETWORK = "Sem conexão com a internet (NetworkError)"
    const val ERR_FORMAT_UNKNOWN = "Erro desconhecido/inesperado"
}

object TAG {
    const val PICPAY_API_TAG = "PicPayApi"
    const val NET_TAG = "PicPayNetwork"
    const val REPO_TAG = "ContactsRepository"
    const val CONTACTS_VM_TAG = "ContactsViewModel"
}