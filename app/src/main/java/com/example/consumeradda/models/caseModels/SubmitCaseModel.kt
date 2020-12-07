package com.example.consumeradda.models.caseModels

import com.google.gson.annotations.SerializedName

data class SubmitCaseModel (
        @field:SerializedName("applicant_first_name")
        val applicantFirstName: String ?= null,

        @field:SerializedName("applicant_last_name")
        val applicantLastName: String ?= null,

        @field:SerializedName("contact_number")
        val contactNumber: String ?= null,

        @field:SerializedName("district")
        val district: String ?= null,

        @field:SerializedName("state")
        val state: String ?= null,

        @field:SerializedName("case_against")
        val caseAgainst: String ?= null,

        @field:SerializedName("case_description")
        val caseOverview: String ?= null,

        @field:SerializedName("case_type")
        val caseType: String ?= null,

        @field:SerializedName("money_involved")
        val moneyInvolved: Int ?= null,

        @field:SerializedName("doc_1")
        val doc1: String ?= null,

        @field:SerializedName("doc_2")
        val doc2: String ?= null,

        @field:SerializedName("doc_3")
        val doc3: String ?= null,

        @field:SerializedName("doc_4")
        val doc4: String ?= null,

        @field:SerializedName("doc_5")
        val doc5: String ?= null
)