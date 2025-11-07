package br.com.auramed.interfaces.dto.response;

import java.util.List;

public class RelatorioResponseDTO {

    public static class HabilidadeDigitalDTO {
        private String skill;
        private Long count;
        public String getSkill() { return skill; }
        public void setSkill(String skill) { this.skill = skill; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
    }

    public static class CanalLembreteDTO {
        private String name;
        private Long value;
        private String fill;
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Long getValue() { return value; }
        public void setValue(Long value) { this.value = value; }
        public String getFill() { return fill; }
        public void setFill(String fill) { this.fill = fill; }
    }

    public static class AcessibilidadeDTO {
        private String type;
        private Long count;
        private Long total;
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public Long getCount() { return count; }
        public void setCount(Long count) { this.count = count; }
        public Long getTotal() { return total; }
        public void setTotal(Long total) { this.total = total; }
    }

    public static class FaqPopularDTO {
        private String question;
        private Long views;
        public String getQuestion() { return question; }
        public void setQuestion(String question) { this.question = question; }
        public Long getViews() { return views; }
        public void setViews(Long views) { this.views = views; }
    }

    public static class UsoChatbotDTO {
        private String month;
        private Long usage;
        public String getMonth() { return month; }
        public void setMonth(String month) { this.month = month; }
        public Long getUsage() { return usage; }
        public void setUsage(Long usage) { this.usage = usage; }
    }

    private List<HabilidadeDigitalDTO> habilidadesDigitais;
    private List<CanalLembreteDTO> canaisLembrete;
    private List<AcessibilidadeDTO> acessibilidades;
    private List<FaqPopularDTO> faqsPopulares;
    private List<UsoChatbotDTO> usoChatbot;
    private List<String> perguntasNaoRespondidas;

    public List<HabilidadeDigitalDTO> getHabilidadesDigitais() { return habilidadesDigitais; }
    public void setHabilidadesDigitais(List<HabilidadeDigitalDTO> habilidadesDigitais) { this.habilidadesDigitais = habilidadesDigitais; }

    public List<CanalLembreteDTO> getCanaisLembrete() { return canaisLembrete; }
    public void setCanaisLembrete(List<CanalLembreteDTO> canaisLembrete) { this.canaisLembrete = canaisLembrete; }

    public List<AcessibilidadeDTO> getAcessibilidades() { return acessibilidades; }
    public void setAcessibilidades(List<AcessibilidadeDTO> acessibilidades) { this.acessibilidades = acessibilidades; }

    public List<FaqPopularDTO> getFaqsPopulares() { return faqsPopulares; }
    public void setFaqsPopulares(List<FaqPopularDTO> faqsPopulares) { this.faqsPopulares = faqsPopulares; }

    public List<UsoChatbotDTO> getUsoChatbot() { return usoChatbot; }
    public void setUsoChatbot(List<UsoChatbotDTO> usoChatbot) { this.usoChatbot = usoChatbot; }

    public List<String> getPerguntasNaoRespondidas() { return perguntasNaoRespondidas; }
    public void setPerguntasNaoRespondidas(List<String> perguntasNaoRespondidas) { this.perguntasNaoRespondidas = perguntasNaoRespondidas; }
}