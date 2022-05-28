// タイトル画像名とその日本語とのマップ
const fnameMap = { de1: 'デ', de2: 'デ', fu: 'フ', ra: 'ラ'};

// 指定されたファイル名に対応するタイトル画像の要素を返す。
function createTitle(fname){
	let title = document.createElement('article');
	title.setAttribute('class', 'col-lg-3 align-self-center');
	title.innerHTML = `<figure class="text-center"><a href="index.html"><img src="img/title/${fname}.png" class="titles" alt="${fnameMap[fname]}"></a></figure>`;
	return title;
}

// タイトル画像を挿入する。
function appendTitle(articles){
	// タイトル画像の種類数
	const titleKindNum = Object.keys(fnameMap).length;
	// おおむね何行ごとにタイトル画像を挿入するか
	const rate = 3;
	// 一列あたりのフラグやタイトル画像の個数
	const lineSize = 4;
	
	// タイトル画像の個数を決定する
	let titleSize = Math.floor(articles.length / (rate * lineSize - 1));
	let mod = (articles.length + titleSize) % lineSize;
	titleSize += (mod == 0)? 0 : lineSize - mod;
	
	// タイトル画像を挿入する
	let posits = [];
	while (posits.length < titleSize){
		let posit = Math.floor(Math.random() * articles.length);
		if (posits.indexOf(posit) < 0) posits.push(posit);
	}
	posits.sort((a, b) => a - b);
	let mains = document.getElementsByTagName('main');
	let titleIdx = 0;
	for (let posit of posits){
		let title = createTitle(Object.keys(fnameMap)[titleIdx ++]);
		if (titleIdx >= titleKindNum) titleIdx = 0;
		mains[0].insertBefore(title, articles[posit]);
	}
}

// 指定されたkindのフラグを取得、それ以外は非表示にする。
function grepFragsByKind(kind){
	let articles = document.getElementsByTagName('article');
	let greped = [];
	for (let article of articles){
		if (article.className.indexOf(kind) < 0){
			article.style.display = 'none';
		} else {
			greped.push(article);
		}
	}
	return greped;
}

// トップページ表示のメイン処理。
function showIndex(){
	let params = new URL(window.location.href).searchParams;
	let kind = params.get('kind');
	
	// GETパラメータkindの値をセッションストレージに格納する
	if (kind){
		sessionStorage.setItem('kind', kind);
	} else {
		sessionStorage.removeItem('kind')
	}
	
	// kindで絞り込みをするか否かによってボタンや文字の色を変える
	let btns = document.getElementsByClassName('btn');
	for (let btn of btns){
		if (kind){
			btn.classList.replace('btn-secondary', 'btn-light');
			btn.style.color = '#333333'
		} else {
			btn.classList.replace('btn-light', 'btn-secondary');
			btn.style.color = '#FFFFFF'
		}
	}
	
	// フラグ一覧を取得し、タイトル画像を追加する
	let articles = (kind)?
		grepFragsByKind(kind) :
		document.getElementsByTagName('article');
	appendTitle(articles);
}