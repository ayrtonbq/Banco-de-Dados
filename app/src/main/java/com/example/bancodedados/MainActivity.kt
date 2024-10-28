package com.example.bancodedados

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.bancodados.DatabaseHelper

class MainActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var listaAlunos: ListView
    lateinit var nomeAluno: EditText
    lateinit var emailAluno: EditText
    lateinit var inserirButton: Button
    lateinit var editarButton: Button
    lateinit var deletarButton: Button
    lateinit var totalAlunosText: TextView

    var alunoSelecionadoId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        listaAlunos = findViewById(R.id.listaAlunos)
        nomeAluno = findViewById(R.id.nomeAluno)
        emailAluno = findViewById(R.id.emailAluno)
        inserirButton = findViewById(R.id.inserirButton)
        editarButton = findViewById(R.id.editarButton)
        deletarButton = findViewById(R.id.deletarButton)
        totalAlunosText = findViewById(R.id.totalAlunosText)

        carregarAlunos()

        inserirButton.setOnClickListener {
            val nome = nomeAluno.text.toString().trim()
            val email = emailAluno.text.toString().trim()

            if (nome.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            } else {
                dbHelper.inserirAluno(nome, email)
                limparCampos()
                carregarAlunos()
            }
        }

        listaAlunos.setOnItemClickListener { _, _, position, _ ->
            val aluno = dbHelper.listarAlunos()[position]
            alunoSelecionadoId = aluno.id
            nomeAluno.setText(aluno.nome)
            emailAluno.setText(aluno.email)
        }

        editarButton.setOnClickListener {
            if (alunoSelecionadoId != null) {
                val nome = nomeAluno.text.toString().trim()
                val email = emailAluno.text.toString().trim()

                if (nome.isEmpty() || email.isEmpty()) {
                    Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
                } else {
                    dbHelper.atualizarAluno(alunoSelecionadoId!!, nome, email)
                    limparCampos()
                    carregarAlunos()
                }
            } else {
                Toast.makeText(this, "Selecione um aluno para editar.", Toast.LENGTH_SHORT).show()
            }
        }

        deletarButton.setOnClickListener {
            if (alunoSelecionadoId != null) {
                dbHelper.deletarAluno(alunoSelecionadoId!!)
                limparCampos()
                carregarAlunos()
            } else {
                Toast.makeText(this, "Selecione um aluno para deletar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun carregarAlunos() {
        val alunos = dbHelper.listarAlunos()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, alunos.map { it.nome })
        listaAlunos.adapter = adapter

        totalAlunosText.text = "Existem ${alunos.size} alunos"
    }

    private fun limparCampos() {
        nomeAluno.text.clear()
        emailAluno.text.clear()
        alunoSelecionadoId = null
    }
}