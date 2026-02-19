# 🚀 Guia de Contribuição e Padrões do Projeto

Este documento define as convenções que utilizamos para manter nosso código organizado, legível e fácil de manter. **Por favor, siga estas diretrizes antes de abrir um Pull Request.**

---

## 🌿 Padrão de Nomenclatura de Branches

Não utilizamos nomes de desenvolvedores nas branches. O foco deve ser sempre a **tarefa** que está sendo executada.

### Estrutura: `tipo/descrição-breve`

| Prefixo     | Descrição                                          | Exemplo                  |
| :---        | :---                                               | :---                     |
| `feat/`     | Nova funcionalidade ou recurso.                    | `feat/login-google`      |
| `fix/`      | Correção de um bug ou erro.                        | `fix/erro-calculo-total` |
| `docs/`     | Alterações apenas na documentação.                 | `docs/update-readme`     |
| `refactor/` | Mudanças no código que não alteram funcionalidade. | `refactor/api-helper`    |
| `test/`     | Adição ou correção de testes.                      | `test/unit-user-service` |

> **Nota:** Use sempre letras minúsculas e separe as palavras com hífen (`kebab-case`).

---

## 💬 Mensagens de Commit

Utilizamos o padrão **Conventional Commits** para que o histórico seja legível por humanos e máquinas.

**Formato:** `tipo: descrição curta em letras minúsculas`

* ✅ **Certo:** `feat: adiciona campo de telefone ao cadastro`
* ❌ **Errado:** `Adicionei o tel` ou `JOAO-COMMIT-1`

---

## 🛠 Fluxo de Trabalho (Workflow)

1. **Sincronize:** Garanta que sua `main` local está atualizada (`git pull origin main`).
2. **Ramifique:** Crie sua branch a partir da `main` seguindo o padrão acima.
3. **Desenvolva:** Faça commits pequenos e descritivos.
4. **Pull Request:** Abra o PR no GitHub e:
   - Dê um título claro.
   - Marque o(s) **Assignees** (quem fez o trabalho).
   - Solicite **Review** de pelo menos um colega.
5. **Merge:** Após aprovado, realize o merge e **delete a branch remota** para manter o repositório limpo.

---

## 📏 Regras de Ouro

* **Não faça push direto na `main`:** Todo código deve passar por um Pull Request.
* **Mantenha o escopo:** Se você está corrigindo um bug, não aproveite para mudar a cor de um botão em outra página. Crie outra branch para isso.
* **Código Limpo:** Remova `console.log`, comentários de teste ou códigos comentados antes de enviar o PR.


# 🛠️ Passo a Passo: Criando e Enviando sua Branch

Siga estes comandos no terminal para garantir que seu trabalho esteja isolado e organizado:

## 1. Sincronize seu repositório local
Antes de iniciar qualquer tarefa, garanta que você tem a versão mais recente do código oficial:
git checkout main
git pull origin main

## 2. Crie e mude para a nova branch
Use o padrão tipo/descricao-da-tarefa. O comando -b cria a branch e já faz o "switch" para ela:
#### Exemplo: git checkout -b feat/login-social
git checkout -b tipo/descricao-da-tarefa

## 3. Salve suas alterações (Commit)
Após codificar, adicione os arquivos ao palco (stage) e crie um commit descritivo:
git add .
git commit -m "tipo: descrição curta do que foi feito"

## 4. Envie a branch para o GitHub (Push)
Na primeira vez que enviar a branch, use o comando abaixo para conectar sua branch local ao servidor:
git push -u origin nome-da-sua-branch

## 5. Abra o Pull Request (PR)
- Vá até a página do seu repositório no GitHub.

- Clique no botão amarelo/verde "Compare & pull request" que aparecerá no topo.

- Adicione uma descrição clara do que foi alterado.

- Marque os Assignees (você e colegas) e solicite um Reviewer.

#### Dica: Se precisar voltar para a branch principal para testar algo, use git checkout main. Nunca trabalhe na main diretamente!
